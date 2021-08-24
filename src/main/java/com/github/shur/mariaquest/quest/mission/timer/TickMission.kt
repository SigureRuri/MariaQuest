package com.github.shur.mariaquest.quest.mission.timer

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.event.TickEvent
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class TickMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit,
    filter: (Player, Unit) -> Boolean
) : Mission<Unit>(goal, onStart, onChangeCount, onClear, onGiveUp, filter) {

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onTick(event: TickEvent) {
            Bukkit.getOnlinePlayers().forEach playerLoop@ { player ->
                val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

                playerData.getQuests().forEach questLoop@ { questData ->
                    val quest = MariaQuest.questManager.get(questData.id) ?: return@playerLoop
                    val status = questData.status as? QuestStatus.InProgress ?: return@playerLoop
                    val mission = quest.missions.getOrNull(status.progress) as? TickMission ?: return@playerLoop

                    if (!mission.filter(player, Unit)) return@questLoop

                    PlayerQuestController.incrementMissionCount(player, quest, 1)
                }
            }
        }

    }

}