package com.github.shur.mariaquest.ui

import com.github.shur.mariaquest.MariaQuest
import com.github.shur.mariaquest.player.PlayerQuestController
import com.github.shur.mariaquest.quest.QuestId
import com.github.shur.whitebait.dsl.window
import com.github.shur.whitebait.inventory.InventoryUI
import com.github.shur.whitebait.inventory.window.TypedWindowOption
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType

class QuestOrderConfirmationUI(
    questId: QuestId
) : InventoryUI {

    val quest = MariaQuest.questManager.get(questId) ?: throw IllegalArgumentException("The quest is not found: $questId")

    override val window by window(TypedWindowOption(InventoryType.HOPPER)) {
        title = "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}クエスト受注"

        defaultSlot {
            icon {
                type = Material.BLACK_STAINED_GLASS_PANE
            }
        }

        slot(0) {
            icon {
                type = Material.GOLD_NUGGET
                name = "${ChatColor.GOLD}${ChatColor.BOLD}受注"
            }
            onClick {
                when (PlayerQuestController.order(player, questId)) {
                    PlayerQuestController.OrderResult.ALREADY_ORDERED -> player.sendMessage("すでにこのクエストを受注しています。")
                    PlayerQuestController.OrderResult.OVER_ORDERABLE_TIMES -> player.sendMessage("クエスト受注可能回数の上限に達しています。")
                    PlayerQuestController.OrderResult.NOT_CLEARED_REQUIRED_QUESTS -> player.sendMessage("前提クエストをクリアしていません。")
                    PlayerQuestController.OrderResult.IN_COOL_TIME -> player.sendMessage("クールタイム中です。")
                    PlayerQuestController.OrderResult.DO_NOT_MEET_REQUIREMENT -> player.sendMessage("条件を満たしていません。")
                    else -> { /* DO NOTHING */ }
                }

                Bukkit.getScheduler().runTask(MariaQuest.instance, Runnable {
                    player.closeInventory()
                })
            }
        }

        slot(2) {
            icon {
                QuestIcons.applyQuestIcon(this, quest)
            }
        }

        slot(4) {
            icon {
                type = Material.BARRIER
                name = "${ChatColor.RED}${ChatColor.BOLD}キャンセル"
            }
            onClick {
                QuestOrderUI(questId).openLater(player)
            }
        }

    }

}