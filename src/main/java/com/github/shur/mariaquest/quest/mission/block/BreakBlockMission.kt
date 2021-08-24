package com.github.shur.mariaquest.quest.mission.block

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BreakBlockMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, BreakBlockEvent) -> Boolean
) : Mission<BreakBlockMission.BreakBlockEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class BreakBlockEvent(val raw: BlockBreakEvent) {
        val brokenBlock = raw.block
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onBreakBlock(event: BlockBreakEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? BreakBlockMission ?: return@forEach

                val missionEvent = BreakBlockEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest, 1)
            }
        }

    }

}