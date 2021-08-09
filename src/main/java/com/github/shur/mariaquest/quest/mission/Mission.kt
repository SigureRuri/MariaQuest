package com.github.shur.mariaquest.quest.mission

import org.bukkit.entity.Player

// TODO: filterはonChangeCountでbool値を返すことでも良いかも
abstract class Mission<T>(
    val goal: Int,
    val onStart: (Player) -> Unit,
    val onChangeCount: (Player, before: Int, after: Int) -> Unit,
    val onFinish: (Player) -> Unit,
    val filter: (Player, T) -> Boolean
)