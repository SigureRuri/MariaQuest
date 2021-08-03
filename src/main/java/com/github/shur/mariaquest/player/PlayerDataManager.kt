package com.github.shur.mariaquest.player

import java.util.*

class PlayerDataManager(
    private val playerDataManipulator: PlayerDataManipulator
) {

    private val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun getPlayerData(uuid: UUID) = playerDataMap[uuid]

}