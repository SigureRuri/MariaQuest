package com.github.shur.mariaquest.quest.mission

import org.bukkit.entity.Player

class BreakBlockMission(
    goal: Int,
    onStart: (Player) -> Unit,
    onChangeCount: (Player, before: Int, after: Int) -> Unit,
    onFinish: (Player) -> Unit
) : Mission(goal, onStart, onChangeCount, onFinish)