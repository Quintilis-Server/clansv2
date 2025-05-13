package org.quintilis.clansv2.managers

import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.Invite

object InviteManager {
    private val playerInvites = mutableListOf<Invite>()
    private val alliesInvites = mutableListOf<AllyInvite>()
    
    var allyExpirationHours: Int = 2;
    var playerExpirationHours: Int = 1;
    
    fun setConfig(allyExpirationHours: Int, playerExpirationHours: Int) {
        this.allyExpirationHours = allyExpirationHours
        this.playerExpirationHours = playerExpirationHours
    }
    
    fun getPlayerInvites() = playerInvites.toList()
    
    fun getAllyInvites() = alliesInvites.toList()
    
    fun addPlayerInvite(invite: Invite) {
        playerInvites.add(invite)
    }
    
    fun addAllyInvite(invite: AllyInvite) {
        alliesInvites.add(invite)
    }
    
    fun acceptInvite(invite: Invite) {
        TODO("Not yet implemented")
    }
    
    fun acceptAllyInvite(invite: AllyInvite) {
        TODO("Not yet implemented")
    }
    
}