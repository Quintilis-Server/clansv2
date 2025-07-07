package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bukkit.Bukkit
import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.string.bold
import java.util.Date

object AllyInviteManager {
    val alliesInvites = MongoManager.allyInviteCollection
    
    val allyExpirationHours: Int = 2;
    
    fun getAllyInvites() = alliesInvites.find().toList()
    
    fun getAllyInvitesBySender(sender: ClanEntity): List<AllyInvite?> {
        return alliesInvites.find(Filters.eq("sender", sender._id)).toList()
    }
    
    fun addAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        val invite = AllyInvite(
            sender = sender._id,
            receiver = receiver._id,
            expireDate = Date(System.currentTimeMillis() + allyExpirationHours * 60L * 60L * 1000L),
        )
        val receiverOwner = Bukkit.getPlayer(PlayerManager.getUUID(sender.owner))
        alliesInvites.insertOne(invite)
        receiverOwner?.sendMessage("O clã ${receiver.name.bold()} quer se aliar com você. use: \"${"/ally invite accept ${receiver.name}".bold()}\" para aceitar.")
    }
    
    fun getAllyInvitesByReceiver(receiver: ClanEntity): List<AllyInvite> {
        return alliesInvites.find(
            Filters.and(
                Filters.eq("receiver", receiver._id),
                Filters.eq("active", true)
            )
        ).toList()
    }
    
    fun rejectAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        Bukkit.getPlayer(PlayerManager.getUUID(sender.owner))?.sendMessage("A sua solicitação de aliação foi recusada.")
        alliesInvites.updateOne(
            Filters.and(Filters.eq("sender", sender._id), Filters.eq("receiver", receiver._id)),
            Updates.set("active", false)
        )
    //        alliesInvites.deleteOne(and(eq("sender", sender._id), eq("receiver", receiver._id)))
    }
    
    fun acceptAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        if(sender.isAlliedWith(receiver)) {
        
        }
    }
}

