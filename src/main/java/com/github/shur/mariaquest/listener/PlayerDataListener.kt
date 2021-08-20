package com.github.shur.mariaquest.listener

import com.github.shur.mariaquest.MariaQuest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerDataListener : Listener {

    @EventHandler
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        val loadResult = MariaQuest.playerDataManager.load(event.uniqueId)

        if (!loadResult) {
            MariaQuest.instance.logger.warning("An exception was occurred while loading PlayerData.")

            event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                "Your MariaQuest PlayerData couldn't load successfully. Please try again."
            )
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (MariaQuest.playerDataManager.contains(player.uniqueId)) return

        val loadResult = MariaQuest.playerDataManager.load(player.uniqueId)

        if (!loadResult) {
            MariaQuest.instance.logger.warning("An exception was occurred while loading PlayerData.")

            player.kickPlayer("Your MariaQuest PlayerData couldn't load successfully. Please try again.")
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val saveResult = MariaQuest.playerDataManager.save(event.player.uniqueId)

        if (!saveResult) {
            MariaQuest.instance.logger.warning("An exception was occurred while saving PlayerData.")
        }
    }

}