package com.github.shur.mariaquest.player.data

sealed class QuestStatus {

    class InProgress(
        progress: Int,
        missionCount: Int
    ) : QuestStatus() {

        var progress: Int = progress
            set(value) { field = 0.coerceAtLeast(value) }

        var missionCount: Int = missionCount
            set(value) { field = 0.coerceAtLeast(value) }

    }

    object Idle : QuestStatus()

}