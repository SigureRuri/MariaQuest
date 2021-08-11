package com.github.shur.mariaquest.quest.mission.location

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, RespawnEvent) -> Boolean
) : Mission<RespawnMission.RespawnEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class RespawnEvent(val raw: PlayerRespawnEvent) {
        val respawnLocation = raw.respawnLocation
        val isAnchorRespawn = raw.isAnchorSpawn
        val isBedRespawn = raw.isBedSpawn
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onRespawn(event: PlayerRespawnEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? RespawnMission ?: return@forEach

                val missionEvent = RespawnEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest.id, 1)
            }
        }

    }

}