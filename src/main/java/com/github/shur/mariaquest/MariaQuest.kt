package com.github.shur.mariaquest

import com.github.shur.mariaquest.event.TickEvent
import com.github.shur.mariaquest.listener.PlayerDataListener
import com.github.shur.mariaquest.player.data.PlayerDataManager
import com.github.shur.mariaquest.player.data.manipulator.PlayerDataManipulator
import com.github.shur.mariaquest.player.data.manipulator.YamlPlayerDataManipulator
import com.github.shur.mariaquest.quest.QuestManager
import com.github.shur.mariaquest.task.CheckTimeLimitTask
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MariaQuest : JavaPlugin() {

    override fun onEnable() {
        instance = this

        questManager = QuestManager()

        playerDataManipulator = YamlPlayerDataManipulator(File("${dataFolder.absolutePath}${File.separator}player"))
        playerDataManager = PlayerDataManager(playerDataManipulator)


        server.pluginManager.registerEvents(PlayerDataListener(), this)

        startRunnable()
    }

    override fun onDisable() {
        playerDataManager.getAll().forEach {
            playerDataManager.save(it.uuid)
        }
    }

    private fun startRunnable() {
        // Start tick runnable
        server.scheduler.runTaskTimer(
            this,
            Runnable {
                server.pluginManager.callEvent(TickEvent())
            },
            0,
            1
        )

        // Start check TimeLimit runnable
        CheckTimeLimitTask().runTaskTimer(this, 20, 20)
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