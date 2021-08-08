package com.github.shur.mariaquest.player.data

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.quest.QuestId
import java.util.*

class PlayerData(
    val uuid: UUID
) {

    val currentQuests: MutableMap<QuestId, QuestData> = mutableMapOf()

    fun getQuest(id: QuestId) = currentQuests[id]

    fun getQuests() = currentQuests.toMap()

    fun getClearedQuests() = currentQuests.filter { it.value.hasCleared() }.toMap()

    // TODO: 進行中のクエストも取得したい

    fun order(id: QuestId): OrderResult {
        val quest = MariaQuest.questManager.get(id) ?: throw IllegalArgumentException("The quest is not found: $id")

        TODO("Implement")
    }

    fun nextMission(id: QuestId) {
        TODO("Implement")
    }

    fun complete(id: QuestId) {
        TODO("Implement")
    }

    enum class OrderResult {

        // TODO: Implement

    }

}