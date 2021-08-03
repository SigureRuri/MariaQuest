package com.github.shur.mariaquest

import com.github.shur.mariaquest.listener.PlayerDataListener
import com.github.shur.mariaquest.player.PlayerDataManager
import com.github.shur.mariaquest.player.PlayerDataManipulator
import com.github.shur.mariaquest.player.YamlPlayerDataManipulator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MariaQuest : JavaPlugin() {

    override fun onEnable() {
        instance = this

        playerDataManipulator = YamlPlayerDataManipulator(File("${dataFolder.absolutePath}${File.separator}player"))
        playerDataManager = PlayerDataManager(playerDataManipulator)


        server.pluginManager.registerEvents(PlayerDataListener(), this)
    }

    override fun onDisable() {
        playerDataManager.getAll().forEach {
            playerDataManager.save(it.uuid)
        }
    }

    companion object {

        lateinit var instance: MariaQuest
            private set

        private lateinit var playerDataManipulator: PlayerDataManipulator

        lateinit var playerDataManager: PlayerDataManager
            private set

    }

}