package com.github.shur.mariaquest.player

import java.util.*

interface PlayerDataManipulator {

    fun load(uuid: UUID): PlayerData

    fun save(playerData: PlayerData)

}