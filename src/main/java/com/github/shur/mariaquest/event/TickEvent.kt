package com.github.shur.mariaquest.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TickEvent : Event() {

    override fun getHandlers() = HANDLERS

    companion object {

        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS

    }

}