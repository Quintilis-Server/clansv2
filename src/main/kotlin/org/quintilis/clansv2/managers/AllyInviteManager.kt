package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.*
import org.bukkit.Bukkit
import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.ClanEntity
import java.util.Date

object AllyInviteManager {
    val alliesInvites = MongoManager.allyInviteCollection
    
    val allyExpirationHours: Int = 2;
    
    fun getAllyInvites() = alliesInvites.find().toList()
    
    fun getAllyInvitesBySender(sender: ClanEntity): List<AllyInvite?> {
        return alliesInvites.find(eq("sender", sender._id)).toList()
    }
    
    fun getAllyInviteByReceiverAndSender(sender: ClanEntity, receiver: ClanEntity): AllyInvite? {
        return this.alliesInvites.find(
            and(
                eq("sender", sender._id),
                eq("receiver", receiver._id),
                eq("active", true),
                gte("expireDate", Date()),
            )
        ).firstOrNull()
    }
    
    
    fun getAllyInvitesByReceiver(receiver: ClanEntity): List<AllyInvite> {
        return alliesInvites.find(
            and(
                eq("receiver", receiver._id),
                eq("active", true),
                gte("expireDate", Date()),
            )
        ).toList()
    }
    fun addAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        val invite = AllyInvite(
            sender = sender._id,
            receiver = receiver._id
        )
        alliesInvites.insertOne(invite)
    }
    
    fun rejectAllyInvite(receiver: ClanEntity, sender: ClanEntity) {
        val invite = this.getAllyInviteByReceiverAndSender(
            sender = sender,
            receiver = receiver
        )
        if(invite == null) {
            throw Error("Convite nao encontrado")
        }
        invite.reject();
        
    }
    
    fun acceptAllyInvite(receiver: ClanEntity, sender: ClanEntity) {
        if(receiver.isAlliedWith(sender)) {
            throw Error("Ja são clãs")
        }
        val invite = this.getAllyInviteByReceiverAndSender(
            sender = sender,
            receiver = receiver
        )
        if(invite == null) {
            throw Error("Convite nao encontrado")
        }
        invite.accept();
    }
    
    
    fun save(allyInvite: AllyInvite) {
        this.alliesInvites.replaceOne(eq(allyInvite._id), allyInvite)
    }
}

