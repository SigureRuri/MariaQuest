package com.github.shur.mariaquest.task

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.player.data.QuestStatus
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.time.LocalDateTime

class CheckTimeLimitTask : BukkitRunnable() {

    override fun run() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!
            playerData.getQuests().forEach quests@{ questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@quests
                if (questData.status !is QuestStatus.InProgress) return@quests
                val startedAt = questData.lastStartedAt ?: return@quests

                quest.timeLimitSeconds?.let { timeLimitSeconds ->
                    if (startedAt.plusSeconds(timeLimitSeconds) < LocalDateTime.now()) {
                        PlayerQuestController.giveUp(player, quest)
                    }
                }
            }
        }
    }

}