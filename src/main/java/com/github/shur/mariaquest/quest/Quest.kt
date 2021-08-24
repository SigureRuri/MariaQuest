package com.github.shur.mariaquest.quest

import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.Material
import org.bukkit.entity.Player

abstract class Quest(
    val id: QuestId
) {

    abstract val name: String

    open val icon: Material = Material.NAME_TAG

    open val description: List<String> = listOf()

    open val missions: List<Mission<*>> = listOf()

    open val timeLimitSeconds: Long? = null

    open val coolTimeSeconds: Long? = null

    open val orderableTimes: Int? = null

    open val requiredQuests: List<QuestId> = listOf()

    open val requirementDescription: List<String> = listOf()

    open fun canBeOrderedBy(player: Player): Boolean = true

    open fun onStart(player: Player) {}

    open fun onClear(player : Player) {}

    open fun onGiveUp(player: Player) {}

}