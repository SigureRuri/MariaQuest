package com.github.shur.mariaquest.player.data

import com.github.shur.mariaquest.quest.QuestId
import java.time.LocalDateTime

class QuestData(
    val id: QuestId
) {

    var status: QuestStatus = QuestStatus.Idle

    var clearCount: Int = 0
        set(value) { field = 0.coerceAtLeast(value) }

    var lastStartedAt: LocalDateTime? = null

    var lastEndedAt: LocalDateTime? = null

    fun hasCleared(): Boolean {
        return clearCount != 0
    }

}