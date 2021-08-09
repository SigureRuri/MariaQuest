package com.github.shur.mariaquest.quest.mission

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.data.QuestStatus
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BreakBlockMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onFinish: (Player) -> Unit,
    filter: (Player, BreakBlockEvent) -> Boolean
) : Mission<BreakBlockMission.BreakBlockEvent>(goal, onStart, onChangeCount, onFinish, filter) {

    data class BreakBlockEvent(val brokenBlock: Block)

    companion object : Listener {

        init {
            MariaQuest.instance.server.pluginManager.registerEvents(this, MariaQuest.instance)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onBreakBlock(event: BlockBreakEvent) {
            val player = event.player
            val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!

            playerData.getQuests().map { it.value }.forEach { questData ->
                val quest = MariaQuest.questManager.get(questData.id) ?: return@forEach
                val status = questData.status as? QuestStatus.InProgress ?: return@forEach

                val mission = quest.missions.getOrNull(status.progress) ?: return@forEach

                if (mission !is BreakBlockMission) return@forEach

                val missionEvent = BreakBlockEvent(event.block)
                if (!mission.filter(player, missionEvent)) return


                // TODO: ここからは別クラスに切り出すべき

                status.missionCount++

                if (mission.goal <= status.missionCount) {
                    // TODO: QuestData#nextMission(QuestId) 的なほうがよいかも

                    if (quest.missions.size <= status.progress + 1) {
                        playerData.complete(questData.id)
                    } else {
                        playerData.nextMission(questData.id)
                    }

                } else {
                    mission.onChangeCount(player, status.progress - 1, status.progress)
                }
            }
        }

    }

}