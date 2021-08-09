package com.github.shur.mariaquest.quest.mission

import org.bukkit.entity.Player

abstract class Mission<T>(
    val goal: Int,
    val onStart: (Player) -> Unit,
    val onChangeCount: (Player, before: Int, after: Int) -> Unit,
    val onClear: (Player) -> Unit,
    val onGiveUp: (Player) -> Unit,
    val filter: (Player, T) -> Boolean
)