package com.github.shur.mariaquest.quest.mission

import org.bukkit.entity.Player

abstract class Mission(
    val goal: Int,
    val onStart: (Player) -> Unit,
    val onChangeCount: (Player, before: Int, after: Int) -> Unit,
    val onFinish: (Player) -> Unit
)