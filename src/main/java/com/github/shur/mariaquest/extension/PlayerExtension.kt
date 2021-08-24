package com.github.shur.mariaquest.extension

import com.github.shur.mariaquest.MariaQuest
import org.bukkit.entity.Player

val Player.mariaQuestPlayerData
    get() = MariaQuest.playerDataManager.get(uniqueId)!!