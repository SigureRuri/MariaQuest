package com.github.shur.mariaquest.player.data.manipulator

import com.github.shur.mariaquest.player.data.PlayerData
import com.github.shur.mariaquest.player.data.QuestData
import com.github.shur.mariaquest.player.data.QuestStatus
import com.github.shur.mariaquest.quest.QuestId
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class YamlPlayerDataManipulator(
    private val dataFolder: File
) : PlayerDataManipulator {

    override fun load(uuid: UUID): PlayerData {
        val dataFile = File("${dataFolder.absolutePath}${File.separator}${uuid}.yml").apply {
            if (!parentFile.exists()) parentFile.mkdirs()
            // ファイルが存在しない = プレイヤーデータが存在しないので新しく作成する
            if (!exists()) return PlayerData(uuid)
        }
        val yaml: FileConfiguration = YamlConfiguration().apply {
            load(dataFile)

            // Check data format version
            if (!isInt("version") || getInt("version") != VERSION) {
                throw IllegalStateException("PlayerData(${uuid}) is unsupported version. The version must be $VERSION")
            }
        }

        val playerData = PlayerData(uuid)

        if (yaml.isConfigurationSection("quests")) {
            yaml.getConfigurationSection("quests")!!.getKeys(false)
                .map { QuestId.of(it) }
                .forEach { questId ->
                    val questSection = yaml.getConfigurationSection("quests.$questId")!!
                    val questData = QuestData(questId).apply {
                        clearCount = questSection.getInt("clearCount")
                        lastStartedAt = LocalDateTime.parse(questSection.getString("lastStartedAt"), DateTimeFormatter.BASIC_ISO_DATE)
                        lastEndedAt = LocalDateTime.parse(questSection.getString("lastEndedAt"), DateTimeFormatter.BASIC_ISO_DATE)

                        when (questSection.getString("status")) {
                            "idle" -> {
                                status = QuestStatus.Idle
                            }
                            "inProgress" -> {
                                val progress = questSection.getInt("progress")
                                val missionCount = questSection.getInt("missionCount")
                                status = QuestStatus.InProgress(progress, missionCount)
                            }
                        }
                    }

                    playerData.currentQuests[questId] = questData
                }
        }

        return playerData
    }

    override fun save(playerData: PlayerData) {
        val dataFile = File("${dataFolder.absolutePath}${File.separator}${playerData.uuid}.yml").apply {
            if (!parentFile.exists()) parentFile.mkdirs()
            if (!exists()) createNewFile()
        }

        val yaml: FileConfiguration = YamlConfiguration().apply {
            // Set data format version
            set("version", VERSION)
        }

        playerData.getQuests().forEach { quest ->
            val questSection = yaml.createSection("quests.${quest.id}")

            questSection.set("clearCount", quest.clearCount)
            questSection.set("lastStartedAt", quest.lastStartedAt?.format(DateTimeFormatter.BASIC_ISO_DATE))
            questSection.set("lastEndedAt", quest.lastEndedAt?.format(DateTimeFormatter.BASIC_ISO_DATE))

            when (val questStatus = quest.status) {
                is QuestStatus.Idle -> {
                    questSection.set("status", "idle")
                }
                is QuestStatus.InProgress -> {
                    questSection.set("status", "inProgress")
                    questSection.set("progress", questStatus.progress)
                    questSection.set("missionCount", questStatus.missionCount)
                }
            }
        }

        // Save .yml file
        yaml.save(dataFile)
    }

    companion object {

        private const val VERSION = 1

    }

}