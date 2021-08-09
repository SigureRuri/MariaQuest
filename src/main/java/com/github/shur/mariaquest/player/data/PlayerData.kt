package com.github.shur.mariaquest.player.data

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.quest.QuestId
import java.time.LocalDateTime
import java.util.*

class PlayerData(
    val uuid: UUID
) {

    val currentQuests: MutableMap<QuestId, QuestData> = mutableMapOf()

    fun getQuest(id: QuestId) = currentQuests[id]

    fun getQuests() = currentQuests.values.toList()

    fun getClearedQuests() = currentQuests.values.filter { it.hasCleared() }

    fun getQuestsInProgress() = currentQuests.values.filter { it.status is QuestStatus.InProgress }

    fun getIdleQuests() = currentQuests.values.filter { it.status is QuestStatus.Idle }

    // クエストを強制的に受注する
    // すでに受注されていても上書きして受注する
    fun order(id: QuestId) {
        val quest = MariaQuest.questManager.get(id) ?: throw IllegalArgumentException("The quest is not found: $id")
        val questData = currentQuests.getOrPut(quest.id) { QuestData(id) }

        questData.status = QuestStatus.InProgress(0, 0)
        questData.lastStartedAt = LocalDateTime.now()
    }

    // クエストを強制的にクリアする
    // すでにクリアされていても上書きしてクリアする
    fun complete(id: QuestId) {
        if (MariaQuest.questManager.has(id)) throw IllegalArgumentException("The quest is not found: $id")
        val questData = currentQuests[id] ?: throw IllegalArgumentException("The QuestData is not found: $id")

        questData.status = QuestStatus.Idle
        questData.clearCount++
        questData.lastEndedAt = LocalDateTime.now()
    }

    // クエストを断念する
    // すでにクリアされていても上書きして断念する
    fun giveUp(id: QuestId) {
        if (MariaQuest.questManager.has(id)) throw IllegalArgumentException("The quest is not found: $id")
        val questData = currentQuests[id] ?: throw IllegalArgumentException("The QuestData is not found: $id")

        questData.status = QuestStatus.Idle
        questData.lastEndedAt = LocalDateTime.now()
    }

    fun nextMission(id: QuestId) {
        val questData = currentQuests[id] ?: throw IllegalArgumentException("The QuestData is not found: $id")

        val status = questData.status
        if (status !is QuestStatus.InProgress) throw IllegalArgumentException("The quest is not started: $id")

        status.progress++
        status.missionCount = 0
    }

}