package com.github.shur.mariaquest.quest

class QuestManager {

    private val quests = mutableMapOf<QuestId, Quest>()

    fun register(quest: Quest) {
        if (quests.contains(quest.id))
            throw IllegalArgumentException("The key of this quest is already registered: ${quest.id}")

        quests[quest.id] = quest
    }

    fun get(id: QuestId) = quests[id]

    fun getAll(): List<Quest> = quests.values.toList()

}