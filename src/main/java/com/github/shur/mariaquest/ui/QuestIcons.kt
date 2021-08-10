package com.github.shur.mariaquest.ui

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.extension.secondsToDaysAndHoursAndMinutesAndSeconds
import com.github.shur.mariaquest.quest.Quest
import com.github.shur.whitebait.dsl.window.IconDSL
import org.bukkit.ChatColor
import org.bukkit.Material

object QuestIcons {

    fun applyQuestIcon(icon: IconDSL, quest: Quest) = icon.apply {
        type = Material.LIGHT_BLUE_CONCRETE
        name = "${ChatColor.WHITE}${quest.name}"

        lore = mutableListOf<String>().apply {
            add("")
            add("${ChatColor.YELLOW}${ChatColor.BOLD} - クエスト詳細")
            if (quest.description.isEmpty()) {
                add("    無し")
            } else {
                addAll(quest.description.map { "    $it" })
            }
            add("")
            if (quest.timeLimitSeconds != null) {
                add("${ChatColor.GREEN}${ChatColor.BOLD} - 時間制限")
                add("    ${quest.timeLimitSeconds.secondsToDaysAndHoursAndMinutesAndSeconds()}")
            }
            if (quest.coolTimeSeconds != null) {
                add("${ChatColor.GREEN}${ChatColor.BOLD} - クールタイム")
                add("    ${quest.coolTimeSeconds.secondsToDaysAndHoursAndMinutesAndSeconds()}")
            }
            if (quest.orderableTimes != null) {
                add("${ChatColor.GREEN}${ChatColor.BOLD} - 受注可能回数")
                add("    ${quest.orderableTimes}")
            }
            if (quest.requiredQuests.isNotEmpty()) {
                add("${ChatColor.GREEN}${ChatColor.BOLD} - 前提クエスト")
                add(quest.requiredQuests.mapNotNull { MariaQuest.questManager.get(it)?.name }.joinToString("") { "    $it" })
            }
            if (quest.requirementDescription.isNotEmpty()) {
                add("${ChatColor.GREEN}${ChatColor.BOLD} - 受注条件")
                addAll(quest.requirementDescription.map { "    $it" })
            }
        }.map { "${ChatColor.WHITE}$it" }.toMutableList()
    }

    fun applyQuestIconWithAllData(icon: IconDSL, quest: Quest) = icon.apply {
        type = Material.LIGHT_BLUE_CONCRETE
        name = "${ChatColor.WHITE}${quest.name}"

        lore = mutableListOf<String>().apply {
            add("")
            add("${ChatColor.YELLOW}${ChatColor.BOLD} - クエスト詳細")
            if (quest.description.isEmpty()) {
                add("    無し")
            } else {
                addAll(quest.description.map { "    $it" })
            }
            add("")
            add("${ChatColor.GREEN}${ChatColor.BOLD} - 時間制限")
            add("    ${quest.timeLimitSeconds?.secondsToDaysAndHoursAndMinutesAndSeconds() ?: "無し"}")
            add("${ChatColor.GREEN}${ChatColor.BOLD} - クールタイム")
            add("    ${quest.coolTimeSeconds?.secondsToDaysAndHoursAndMinutesAndSeconds() ?: "無し"}")
            add("${ChatColor.GREEN}${ChatColor.BOLD} - 受注可能回数")
            add("    ${quest.orderableTimes ?: "無限"}")
            add("${ChatColor.GREEN}${ChatColor.BOLD} - 前提クエスト")
            if (quest.requiredQuests.isEmpty()) {
                add("    無し")
            } else {
                add(quest.requiredQuests.mapNotNull { MariaQuest.questManager.get(it)?.name }
                    .joinToString("") { "    $it" })
            }
            add("${ChatColor.GREEN}${ChatColor.BOLD} - 受注条件")
            if (quest.requirementDescription.isEmpty()) {
                add("    無し")
            } else {
                addAll(quest.requirementDescription.map { "    $it" })
            }
        }.map { "${ChatColor.WHITE}$it" }.toMutableList()
    }

}