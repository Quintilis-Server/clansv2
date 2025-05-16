package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.Invite
import java.util.UUID

object InviteManager {
    private val playerInvites = mutableListOf<Invite>()
    private val alliesInvites = mutableListOf<AllyInvite>()
    
    private val clanCollection = MongoManager.clanCollection
    private val playerCollection = MongoManager.playerCollection
    
    private var allyExpirationHours: Int = 2;
    private var playerExpirationHours: Int = 1;
    
    fun setConfig(allyExpirationHours: Int, playerExpirationHours: Int) {
        this.allyExpirationHours = allyExpirationHours
        this.playerExpirationHours = playerExpirationHours
    }
    
    //player invite
    
    fun getPlayerInvites() = playerInvites.toList()
    
    fun addPlayerInvite(invite: Invite) {
        playerInvites.add(invite)
    }
    
    fun getPlayerInvitesByReceiver(receiver: UUID): List<Invite> {
        return playerInvites.filter { it.receiver == receiver }
    }
    
    fun getPlayerInviteBySender(sender: UUID): Invite? {
        return playerInvites.find { it.sender == sender }
    }
    
    fun acceptInvite(invite: Invite) {
        TODO("Not yet implemented")
    }
    
    
    //ally invite
    
    fun getAllyInvites() = alliesInvites.toList()
    
    fun addAllyInvite(receiver: ClanEntity, sender: ClanEntity) {
        val invite = AllyInvite(sender._id, receiver._id)
        val receiverOwner = Bukkit.getPlayer(receiver.owner)
        alliesInvites.add(invite)
        receiverOwner?.sendMessage("O clã ${sender.name} quer se aliar com você. use: \"/ally accept ${sender.name}\" para aceitar.")
    }
    
    fun getAllyInvitesByReceiver(receiver: ObjectId): List<AllyInvite> {
        return alliesInvites.filter { it.receiver == receiver }
    }
    
    fun getAllyInvitesBySender(sender: ObjectId): AllyInvite? {
        return alliesInvites.find { it.sender == sender }
    }
    
    
    fun acceptAllyInvite(invite: AllyInvite) {
        
        val clanSender = ClanManager.getClanById(invite.sender)
        val clanReceiver = ClanManager.getClanById(invite.receiver)
        
        
        clanCollection.findOneAndUpdate(eq("id", invite.sender), push("allies", invite.receiver))
        clanCollection.findOneAndUpdate(eq("id", invite.receiver), push("allies", invite.sender))
        
        for(member in clanSender!!.members) {
            Bukkit.getPlayer(member)?.sendMessage("O seu clã se aliou ${clanReceiver!!.name}.")
        }
        for(member in clanReceiver!!.members) {
            Bukkit.getPlayer(member)?.sendMessage("O seu clã se aliou ${clanSender.name}.")
        }
        
        alliesInvites.removeIf { it.sender == invite.sender && it.receiver == invite.receiver }
    }
    
    fun rejectAllyInvite(invite: AllyInvite) {
        val clanSender = ClanManager.getClanById(invite.sender)
        Bukkit.getPlayer(clanSender!!.owner)?.sendMessage("A sua solicitação de aliação foi recusada.")
        alliesInvites.removeIf { it.sender == invite.sender && it.receiver == invite.receiver }
    }
    
}