package com.github.shur.mariaquest

import com.github.shur.mariaquest.event.TickEvent
import com.github.shur.mariaquest.listener.PlayerDataListener
import com.github.shur.mariaquest.player.data.PlayerDataManager
import com.github.shur.mariaquest.player.data.manipulator.PlayerDataManipulator
import com.github.shur.mariaquest.player.data.manipulator.YamlPlayerDataManipulator
import com.github.shur.mariaquest.quest.QuestManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MariaQuest : JavaPlugin() {

    override fun onEnable() {
        instance = this

        questManager = QuestManager()

        playerDataManipulator = YamlPlayerDataManipulator(File("${dataFolder.absolutePath}${File.separator}player"))
        playerDataManager = PlayerDataManager(playerDataManipulator)


        server.pluginManager.registerEvents(PlayerDataListener(), this)

        startTickRunnable()
    }

    override fun onDisable() {
        playerDataManager.getAll().forEach {
            playerDataManager.save(it.uuid)
        }
    }

    private fun startTickRunnable() {
        server.scheduler.runTaskTimer(
            this,
            Runnable {
                server.pluginManager.callEvent(TickEvent())
            },
            0,
            1
        )
    }

    companion object {

        lateinit var instance: MariaQuest
            private set

        lateinit var questManager: QuestManager
            private set

        private lateinit var playerDataManipulator: PlayerDataManipulator

        lateinit var playerDataManager: PlayerDataManager
            private set

    }

}