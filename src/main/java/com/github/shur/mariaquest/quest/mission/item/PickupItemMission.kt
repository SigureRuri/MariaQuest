package com.github.shur.mariaquest.quest.mission.item

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class PickupItemMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, PickupItemEvent) -> Boolean
) : Mission<PickupItemMission.PickupItemEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class PickupItemEvent(val raw: EntityPickupItemEvent) {
        val item = raw.item
        val remaining = raw.remaining
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onPickupItem(event: EntityPickupItemEvent) {
            val player = event.entity as? Player ?: return
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? PickupItemMission ?: return@forEach

                val missionEvent = PickupItemEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest.id, 1)
            }
        }

    }

}