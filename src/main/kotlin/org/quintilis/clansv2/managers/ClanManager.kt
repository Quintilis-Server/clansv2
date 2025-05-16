package org.quintilis.clansv2.managers

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity

object ClanManager {
    private val clan: MongoCollection<ClanEntity> = MongoManager.clanCollection;
    private val player: MongoCollection<PlayerEntity> = MongoManager.playerCollection;
    private val invites: InviteManager = InviteManager
    
    fun create(clan: ClanEntity) {
        clan.members.add(clan.owner)
        this.clan.insertOne(clan)
    }
    
    fun delete(clan: ClanEntity){
        clan.members.forEach {
            player.updateOne(
                eq("mineId", it),
                set("clan", null)
            )
        }
        sendMessageToMembers(clan, "O seu clã foi deletado.")
        this.clan.deleteOne(eq("_id", clan._id))
    }
    
    fun listClans(): List<ClanEntity> {
        return this.clan.find().toList()
    }
    
    fun getClanById(id: ObjectId): ClanEntity? {
        return this.clan.find(eq("_id", id)).first()
    }
    
    fun getClanByName(name: String): ClanEntity? {
        return this.clan.find(eq("name", name)).first()
    }
    
    fun getClanByOwner(owner: Player): ClanEntity? {
        return this.clan.find(eq("owner", owner.uniqueId)).first()
    }
    
    fun exists(clanName: String): Boolean {
        return this.clan.find(eq("name", clanName)).first() != null;
    }
    
    fun isOwner(player: Player, clanId: ObjectId): Boolean {
        val clan = this.getClanById(clanId)
        return player.uniqueId == clan?.owner
    }
    
    fun sendMessageToMembers(clan: ClanEntity, message: String) {
        clan.members.forEach {
            Bukkit.getPlayer(it)?.sendMessage(message)
        }
    }
    
    fun addAlly(clan: ClanEntity, ally: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            set("allies", ally._id)
        )
        this.clan.updateOne(
            eq("_id", ally._id),
            set("allies", clan._id)
        )
    }
    
}