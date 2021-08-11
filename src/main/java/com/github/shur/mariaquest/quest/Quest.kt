package com.github.shur.mariaquest.quest

import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.Material
import org.bukkit.entity.Player

data class Quest(
    val id: QuestId,
    val icon: Material,
    val name: String,
    val description: List<String>,
    val missions: List<Mission<*>>,
    val timeLimitSeconds: Long?,
    val coolTimeSeconds: Long?,
    val orderableTimes: Int?,
    val requiredQuests: List<QuestId>,
    val requirement: (Player) -> Boolean,
    val requirementDescription: List<String>,
    val onStart: (Player) -> Unit,
    val onClear: (Player) -> Unit,
    val onGiveUp: (Player) -> Unit
)