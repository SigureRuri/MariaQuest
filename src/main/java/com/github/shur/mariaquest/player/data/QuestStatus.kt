package com.github.shur.mariaquest.player.data

sealed class QuestStatus {

    class InProgress(
        var progress: Int
    ) : QuestStatus()

    object IDLE : QuestStatus()

}