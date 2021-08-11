package com.github.shur.mariaquest.quest.mission.interact

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class InteractEntityMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, InteractEntityEvent) -> Boolean
) : Mission<InteractEntityMission.InteractEntityEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class InteractEntityEvent(val raw: PlayerInteractEntityEvent) {
        val rightClicked = raw.rightClicked
        val hand = raw.hand
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onInteractEntity(event: PlayerInteractEntityEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? InteractEntityMission ?: return@forEach

                val missionEvent = InteractEntityEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest.id, 1)
            }
        }

    }

}