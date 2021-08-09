package com.github.shur.mariaquest.player.data.manipulator

import com.github.shur.mariaquest.player.data.PlayerData
import java.util.*

interface PlayerDataManipulator {

    fun load(uuid: UUID): PlayerData

    fun save(playerData: PlayerData)

}