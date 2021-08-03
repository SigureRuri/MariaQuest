package com.github.shur.mariaquest.player

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class YamlPlayerDataManipulator(
    private val plugin: JavaPlugin
) : PlayerDataManipulator {

    private val playerDataFolder = File("${plugin.dataFolder.absolutePath}${java.io.File.separator}player")

    override fun load(uuid: UUID): PlayerData {
        TODO("Not yet implemented")
    }

    override fun save(playerData: PlayerData) {
        TODO("Not yet implemented")
    }

}