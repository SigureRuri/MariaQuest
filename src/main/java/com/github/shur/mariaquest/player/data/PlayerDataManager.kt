package com.github.shur.mariaquest.player.data

import com.github.shur.mariaquest.player.data.manipulator.PlayerDataManipulator
import java.util.*

class PlayerDataManager(
    private val playerDataManipulator: PlayerDataManipulator
) {

    private val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun get(uuid: UUID) = playerDataMap[uuid]

    fun getAll() = playerDataMap.values.toList()

    fun contains(uuid: UUID) = playerDataMap.contains(uuid)

    fun load(uuid: UUID): Boolean =
        try {
            val playerData = playerDataManipulator.load(uuid)
            playerDataMap[uuid] = playerData
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


    fun save(uuid: UUID): Boolean {
        try {
            val playerData = playerDataMap[uuid] ?: return false
            playerDataManipulator.save(playerData)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}