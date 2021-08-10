package com.github.shur.mariaquest.ui

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.extension.secondsToDaysAndHoursAndMinutesAndSeconds
import com.github.shur.mariaquest.quest.QuestId
import com.github.shur.whitebait.dsl.window
import com.github.shur.whitebait.inventory.InventoryUI
import com.github.shur.whitebait.inventory.window.TypedWindowOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType

class QuestOrderUI(
    questId: QuestId
) : InventoryUI {

    val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")

    override val window by window(TypedWindowOption(InventoryType.DROPPER)) {
        title = "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}クエスト受注"

        defaultSlot {
            icon {
                type = Material.BLACK_STAINED_GLASS_PANE
            }
        }

        slot(4) {
            icon {
                type = Material.LIGHT_BLUE_CONCRETE
                name = quest.name

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
                }

                onClick {
                    QuestOrderConfirmationUI(questId).openLater(player)
                }
            }
        }
    }

}