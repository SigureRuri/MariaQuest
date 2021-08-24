package com.github.shur.mariaquest.quest.mission.extra

import com.github.shur.mariaquest.quest.mission.Mission
import org.bukkit.entity.Player

class ImpossibleMission(
    onStart: (Player) -> Unit,
    onClear: (Player) -> Unit,
    onGiveUp: (Player) -> Unit
) : Mission<Unit>(0, onStart, { _, _, _ -> }, onClear, onGiveUp, { _, _ -> true })