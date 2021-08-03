package com.github.shur.mariaquest.player

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class YamlPlayerDataManipulator(
    private val dataFolder: File
) : PlayerDataManipulator {

    override fun load(uuid: UUID): PlayerData {
        val dataFile = File("${dataFolder.absolutePath}${File.separator}${uuid}.yml").apply {
            if (!parentFile.exists()) parentFile.mkdirs()
            if (!exists()) createNewFile()
        }
        val yaml = YamlConfiguration().load(dataFile)

        val playerData = PlayerData(uuid).apply {
            // TODO: Apply data by using `yaml`
        }

        return playerData
    }

    override fun save(playerData: PlayerData) {
        val dataFile = File("${dataFolder.absolutePath}${File.separator}${playerData.uuid}.yml").apply {
            if (!parentFile.exists()) parentFile.mkdirs()
            if (!exists()) createNewFile()
        }
        val yaml = YamlConfiguration()

        // TODO: Save data by using `yaml`

        // Save .yml file
        yaml.save(dataFile)
    }

}