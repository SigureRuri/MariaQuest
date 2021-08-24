package com.github.shur.mariaquest.quest.mission.inventory

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class ClickInventoryMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, ClickInventoryEvent) -> Boolean
) : Mission<ClickInventoryMission.ClickInventoryEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class ClickInventoryEvent(val raw: InventoryClickEvent) {
        val click = raw.click
        val clickedInventory = raw.clickedInventory
        val currentItem = raw.currentItem
        val cursor = raw.cursor
        val hotbarButton = raw.hotbarButton
        val rawSlot = raw.rawSlot
        val slot = raw.slot
        val slotType = raw.slotType
        val isLeftClick = raw.isLeftClick
        val isRightClick = raw.isRightClick
        val isShiftClick = raw.isShiftClick
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onClickInventory(event: InventoryClickEvent) {
            val player = event.whoClicked as? Player ?: return
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? ClickInventoryMission ?: return@forEach

                val missionEvent = ClickInventoryEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest, 1)
            }
        }

    }

}