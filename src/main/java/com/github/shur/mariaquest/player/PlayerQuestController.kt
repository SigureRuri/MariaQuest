package com.github.shur.mariaquest.player

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.extension.mariaQuestPlayerData
import com.github.shur.mariaquest.player.data.QuestData
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.Quest
import com.github.shur.mariaquest.quest.QuestId
import org.bukkit.entity.Player
import java.time.LocalDateTime

object PlayerQuestController {

    /*
     * クエストを受注する。
     *
     * 受注可能かどうかの検証は、
     * - クエストをすでに受注していない
     * - クエスト受注可能回数が規定を超えていない
     * - 前提クエストの確認
     * - クールタイムの確認
     * - 要件を満たしているか
     * の順で行われる。
     */
    fun order(player: Player, quest: Quest): OrderResult {
        if (!MariaQuest.questManager.has(quest.id)) throw IllegalArgumentException("The quest is not found: ${quest.id}")

        val playerData = player.mariaQuestPlayerData
        val questData = playerData.currentQuests.getOrPut(quest.id) { QuestData(quest.id) }

        // クエストをすでに受注していない
        if (questData.status !is QuestStatus.Idle) return OrderResult.ALREADY_ORDERED

        // クエスト受注回数が規定を超えていない
        if (quest.orderableTimes != null && questData.clearCount >= quest.orderableTimes!!) return OrderResult.OVER_ORDERABLE_TIMES

        // 前提クエストの確認
        quest.requiredQuests.forEach {
            if (!MariaQuest.questManager.has(it)) return OrderResult.NOT_CLEARED_REQUIRED_QUESTS
            val requiredQuestData = playerData.currentQuests[it] ?: return OrderResult.NOT_CLEARED_REQUIRED_QUESTS
            if (!requiredQuestData.hasCleared()) return OrderResult.NOT_CLEARED_REQUIRED_QUESTS
        }

        // クールタイムの確認
        val coolTimeSeconds = quest.coolTimeSeconds
        val lastEndedAt = questData.lastEndedAt
        if (coolTimeSeconds != null && lastEndedAt != null) {
            if (LocalDateTime.now() < lastEndedAt.plusSeconds(coolTimeSeconds)) return OrderResult.IN_COOL_TIME
        }

        // 要件を満たしている
        if (!quest.canBeOrderedBy(player)) return OrderResult.DO_NOT_MEET_REQUIREMENT


        quest.onStart(player)
        quest.missions[0].onStart(player)

        playerData.order(quest.id)

        return OrderResult.SUCCESS
    }

    /*
     * クエストを終了する。
     *
     * この時、ミッションが残っていてもクエストは終了される。
     */
    fun complete(player: Player, quest: Quest) {
        val playerData = player.mariaQuestPlayerData
        val status = playerData.currentQuests[quest.id]?.status as? QuestStatus.InProgress ?: return

        quest.missions.getOrNull(status.progress)?.let { it.onClear(player) }
        quest.onClear(player)

        playerData.complete(quest.id)
    }

    fun giveUp(player: Player, quest: Quest) {
        val playerData = player.mariaQuestPlayerData
        val status = playerData.currentQuests[quest.id]?.status as? QuestStatus.InProgress ?: return

        quest.missions.getOrNull(status.progress)?.let { it.onGiveUp(player) }
        quest.onGiveUp(player)

        playerData.giveUp(quest.id)
    }

    fun nextMission(player: Player, quest: Quest) {
        val playerData = player.mariaQuestPlayerData
        val questData = playerData.currentQuests[quest.id] ?: return
        val status = questData.status as? QuestStatus.InProgress ?: return

        // 次のミッションへ/クエスト終了
        if (status.progress >= quest.missions.lastIndex) {
            complete(player, quest)
        } else {
            quest.missions[status.progress].onClear(player)
            quest.missions[status.progress + 1].onStart(player)
            playerData.nextMission(quest.id)
        }
    }

    // countは負の値も可
    fun incrementMissionCount(player: Player, quest: Quest, count: Int) {
        val playerData = player.mariaQuestPlayerData
        val questData = playerData.currentQuests[quest.id] ?: return
        val status = questData.status as? QuestStatus.InProgress ?: return
        val currentMission = quest.missions.getOrNull(status.progress) ?: return

        currentMission.onChangeCount(player, status.missionCount, status.missionCount + count)

        status.missionCount += count

        if (status.missionCount >= currentMission.goal) {
            nextMission(player, quest)
        }
    }

    enum class OrderResult {
        SUCCESS,
        ALREADY_ORDERED,
        OVER_ORDERABLE_TIMES,
        NOT_CLEARED_REQUIRED_QUESTS,
        IN_COOL_TIME,
        DO_NOT_MEET_REQUIREMENT
    }

}
