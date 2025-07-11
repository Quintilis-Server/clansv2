package org.quintilis.clansv2.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.quintilis.clansv2.events.ClanCreateEvent
import org.quintilis.clansv2.events.ClanDeleteEvent
import org.quintilis.clansv2.events.ClanEditEvent
import org.quintilis.clansv2.luckperms.LuckPermsManager

class ClanEventListener(
    private val luckPermsManager: LuckPermsManager
): Listener {
    
    @EventHandler
    fun onClanCreate(event: ClanCreateEvent) {
        luckPermsManager.createGroupFromClan(event.clan)
    }
    
    @EventHandler
    fun onClanDelete(event: ClanDeleteEvent) {
    
    }
    
    @EventHandler
    fun onClanEdit(event: ClanEditEvent) {
    
    }
}