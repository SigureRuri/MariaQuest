package com.github.shur.mariaquest

import com.github.shur.mariaquest.player.PlayerDataManager
import com.github.shur.mariaquest.player.YamlPlayerDataManipulator
import org.bukkit.plugin.java.JavaPlugin

class MariaQuest : JavaPlugin() {

    override fun onEnable() {

        playerDataManager = PlayerDataManager(YamlPlayerDataManipulator(this))

    }

    companion object {

        private lateinit var playerDataManager: PlayerDataManager

    }

}