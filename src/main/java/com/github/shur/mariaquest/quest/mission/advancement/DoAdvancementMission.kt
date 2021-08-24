package com.github.shur.mariaquest.quest.mission.advancement

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class DoAdvancementMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, DoAdvancementEvent) -> Boolean
) : Mission<DoAdvancementMission.DoAdvancementEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class DoAdvancementEvent(val raw: PlayerAdvancementDoneEvent) {
        val advancement = raw.advancement
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onDoAdvancement(event: PlayerAdvancementDoneEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? DoAdvancementMission ?: return@forEach

                val missionEvent = DoAdvancementEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest, 1)
            }
        }

    }

}