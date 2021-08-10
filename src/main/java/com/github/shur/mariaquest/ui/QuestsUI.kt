package com.github.shur.mariaquest.ui

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.whitebait.dsl.window
import com.github.shur.whitebait.inventory.InventoryUI
import com.github.shur.whitebait.inventory.window.SizedWindowOption
import org.bukkit.ChatColor
import org.bukkit.Material

class QuestsUI(val page: Int = 1) : InventoryUI {

    override val window by window(SizedWindowOption(5 * 9)) {
        title = "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}クエスト一覧"

        val quests = MariaQuest.questManager.getAll()
        val maxPage = (quests.lastIndex + (5 * 9)) / (5 * 9)

        if (page > 1) {
            slot(52) {
                icon {
                    type = Material.ARROW
                    name = "${ChatColor.RED}前のページへ"
                }
                onClickFilterNotDoubleClick {
                    QuestsUI(page - 1).openLater(player)
                }
            }
        }

        if (page < maxPage) {
            slot(53) {
                icon {
                    type = Material.ARROW
                    name = "${ChatColor.RED}次のページへ"
                }
                onClickFilterNotDoubleClick {
                    QuestsUI(page + 1).openLater(player)
                }
            }
        }

        (0 until (5 * 9)).forEach { slotIndex ->
            val questIndex = slotIndex + ((5 * 9) * (page - 1))
            quests.getOrNull(questIndex)?.let { quest ->
                slot(slotIndex) {
                    icon {
                        QuestIcons.applyQuestIconWithAllData(this, quest)
                    }
                }
            }
        }
    }

}