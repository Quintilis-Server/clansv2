package org.quintilis.clansv2.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.quintilis.clansv2.managers.PlayerManager

class PlayerEventListener: Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        PlayerManager.saveIfNotExists(event.player)
    }
}