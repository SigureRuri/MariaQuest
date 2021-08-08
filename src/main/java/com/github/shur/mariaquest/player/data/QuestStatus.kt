package com.github.shur.mariaquest.player.data

sealed class QuestStatus {

    class InProgress(
        var progress: Int,
        var missionCount: Int
    ) : QuestStatus()

    object IDLE : QuestStatus()

}