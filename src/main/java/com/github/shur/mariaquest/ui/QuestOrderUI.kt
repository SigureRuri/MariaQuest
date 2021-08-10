package com.github.shur.mariaquest.ui

import com.github.shur.mariaquest.MariaQuest
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
                QuestIcons.applyQuestIcon(this, quest)
            }
            onClick {
                QuestOrderConfirmationUI(questId).openLater(player)
            }
        }
    }

}