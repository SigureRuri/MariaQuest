package com.github.shur.mariaquest.quest.mission.item

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRecipeDiscoverEvent

class DiscoverRecipeMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, DiscoverRecipeEvent) -> Boolean
) : Mission<DiscoverRecipeMission.DiscoverRecipeEvent>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    data class DiscoverRecipeEvent(val raw: PlayerRecipeDiscoverEvent) {
        val recipe = raw.recipe
    }

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onDiscoverRecipe(event: PlayerRecipeDiscoverEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach
                val mission = quest.missions.getOrNull(status.progress) as? DiscoverRecipeMission ?: return@forEach

                val missionEvent = DiscoverRecipeEvent(event)
                if (!mission.filter(player, missionEvent)) return

                PlayerQuestController.incrementMissionCount(player, quest, 1)
            }
        }

    }

}