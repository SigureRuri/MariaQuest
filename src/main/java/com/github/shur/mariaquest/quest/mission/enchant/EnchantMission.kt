package com.github.shur.mariaquest.quest.mission.enchant

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import com.github.shur.mariaquest.quest.mission.block.PlaceBlockMission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.enchantment.EnchantItemEvent

class EnchantMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, EnchantEvent) -> Boolean
) : Mission<EnchantMission.EnchantEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class EnchantEvent(val raw: EnchantItemEvent) {
        val view = raw.view
        val enchantingTable = raw.enchantBlock
        val item = raw.item
        val expLevelCost = raw.expLevelCost
        val enchants = raw.enchantsToAdd
        val placedButton = raw.whichButton()
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onEnchantItem(event: EnchantItemEvent) {
            val player = event.enchanter
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? EnchantMission ?: return@forEach

                val missionEvent = EnchantEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest.id, 1)
            }
        }

    }

}