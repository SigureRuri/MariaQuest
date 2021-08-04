package com.github.shur.mariaquest.quest

import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player

data class Quest(
    val id: QuestId,
    val name: String,
    val description: List<String>,
    val missions: List<Mission>,
    val coolTimeSeconds: Int?,
    val orderableTimes: Int?,
    val requiredQuests: List<Quest>,
    val requirement: (Player) -> Boolean,
    val requirementDescription: List<String>
)