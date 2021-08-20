package com.github.shur.mariaquest.player

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.data.QuestData
import com.github.shur.mariaquest.player.data.QuestStatus
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
    fun order(player: Player, questId: QuestId): OrderResult {
        val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")
        val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!
        val questData = playerData.currentQuests.getOrPut(quest.id) { QuestData(questId) }

        // クエストをすでに受注していない
        if (questData.status !is QuestStatus.Idle) return OrderResult.ALREADY_ORDERED

        // クエスト受注回数が規定を超えていない
        if (quest.orderableTimes != null && questData.clearCount >= quest.orderableTimes) return OrderResult.OVER_ORDERABLE_TIMES

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
        if (!quest.requirement(player)) return OrderResult.DO_NOT_MEET_REQUIREMENT


        quest.onStart(player)
        quest.missions[0].onStart(player)

        playerData.order(questId)

        return OrderResult.SUCCESS
    }

    /*
     * クエストを終了する。
     *
     * 終了可能かどうかの検証は、
     * - クエストが進行中であるか
     * の順で行われる。
     *
     * この時、ミッションが残っていてもクエストは終了される。
     */
    fun complete(player: Player, questId: QuestId): CompleteResult {
        val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")
        val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!
        val questData = playerData.currentQuests[questId] ?: return CompleteResult.NOT_ORDERED

        // クエストを受注しているか
        val status = questData.status as? QuestStatus.InProgress ?: return CompleteResult.NOT_ORDERED

        if (status.progress < quest.missions.lastIndex) return CompleteResult.NOT_CLEARED_ALL_MISSIONS

        quest.onClear(player)

        playerData.complete(questId)

        return CompleteResult.SUCCESS
    }

    fun giveUp(player: Player, questId: QuestId): GiveUpResult {
        val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")
        val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!
        val questData = playerData.currentQuests[questId] ?: return GiveUpResult.NOT_ORDERED

        // クエストを受注しているか
        val status = questData.status as? QuestStatus.InProgress ?: return GiveUpResult.NOT_ORDERED

        quest.missions.getOrNull(status.progress)?.let { it.onGiveUp(player) }
        quest.onGiveUp(player)

        playerData.giveUp(questId)

        return GiveUpResult.SUCCESS
    }

    // countは負の値も可
    fun incrementMissionCount(player: Player, questId: QuestId, count: Int): IncrementMissionCountResult {
        val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")
        val playerData = MariaQuest.playerDataManager.get(player.uniqueId)!!
        val questData = playerData.currentQuests[questId] ?: return IncrementMissionCountResult.NOT_ORDERED
        val status = questData.status as? QuestStatus.InProgress ?: return IncrementMissionCountResult.NOT_ORDERED

        // 現在のミッションが存在しなければクエスト終了
        val currentMission = quest.missions.getOrNull(status.progress)
        if (currentMission == null) {
            complete(player, questId)
            return IncrementMissionCountResult.QUEST_COMPLETE
        }

        currentMission.onChangeCount(player, status.missionCount, status.missionCount + count)

        status.missionCount += count

        return if (status.missionCount >= currentMission.goal) {
            currentMission.onClear(player)

            // 次のミッションへ/クエスト終了
            if (status.progress >= quest.missions.lastIndex) {
                complete(player, questId)

                IncrementMissionCountResult.QUEST_COMPLETE
            } else {
                quest.missions.getOrNull(status.progress + 1)!!.onStart(player)
                playerData.nextMission(questId)

                IncrementMissionCountResult.NEXT_MISSION
            }
        } else {
            IncrementMissionCountResult.SUCCESS
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

    enum class CompleteResult {
        SUCCESS,
        NOT_ORDERED,
        NOT_CLEARED_ALL_MISSIONS
    }

    enum class GiveUpResult {
        SUCCESS,
        NOT_ORDERED
    }

    enum class IncrementMissionCountResult {
        SUCCESS,
        QUEST_COMPLETE,
        NOT_ORDERED,
        NEXT_MISSION,
    }

}
