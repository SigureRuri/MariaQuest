package com.github.shur.mariaquest.player.data

import com.github.shur.mariaquest.quest.QuestId
import java.time.LocalDateTime

// TODO: プレイヤーのuuidは必要か
class QuestData(
    val id: QuestId
) {

    var status: QuestStatus = QuestStatus.IDLE

    var clearCount: Int = 0
        set(value) { field = 0.coerceAtLeast(value) }

    var lastStartedAt: LocalDateTime? = null

    var lastEndedAt: LocalDateTime? = null


    // TODO: lastStartedAtとlastEndedAtの検証も含めたほうがよい？
    fun hasCleared(): Boolean {
        return clearCount != 0
    }

}