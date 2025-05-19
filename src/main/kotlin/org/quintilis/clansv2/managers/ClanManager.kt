package org.quintilis.clansv2.managers

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import java.util.UUID

object ClanManager {
    private val clan: MongoCollection<ClanEntity> = MongoManager.clanCollection;
    private val player: MongoCollection<PlayerEntity> = MongoManager.playerCollection;
    private val invites: InviteManager = InviteManager
    
    fun create(clan: ClanEntity, owner: Player) {
        clan.members.add(clan.owner!!)
        PlayerManager.setClan(owner.uniqueId, clan)
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
        val player = PlayerManager.getPlayerByMineId(owner.uniqueId)!!
        return this.clan.find(eq("owner", player._id)).first()
    }
    fun getAllClans(): List<ClanEntity> {
        return this.clan.find().toList()
    }
    
    fun exists(clanName: String): Boolean {
        return this.clan.find(eq("name", clanName)).first() != null;
    }
    
    fun isOwner(player: Player): Boolean {
        return this.clan.find(eq("owner", player.uniqueId)).first() != null;
    }
    
    fun isOwner(player: Player, clanId: ObjectId): Boolean {
        val clan = this.getClanById(clanId)
        return player.uniqueId == clan?.owner
    }
    
    fun sendMessageToMembers(clan: ClanEntity, message: String) {
        clan.members.forEach {
            Bukkit.getPlayer(PlayerManager.getPlayerById(it)?.mineId!!)?.sendMessage(message)
        }
    }
    
    fun addAlly(clan: ClanEntity, ally: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            push("allies", ally._id)
        )
        this.clan.updateOne(
            eq("_id", ally._id),
            push("allies", clan._id)
        )
    }
    
    fun addEnemy(clan: ClanEntity, enemy: ClanEntity){
        this.clan.updateOne(
            eq("_id", clan._id),
            push("enemies", enemy._id)
        )
        this.clan.updateOne(
            eq("_id", enemy._id),
            push("enemies", clan._id)
        )
    }
    
    fun removeAlly(clan: ClanEntity, ally: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            pull("allies", ally._id)
        )
        this.clan.updateOne(
            eq("_id", ally._id),
            push("allies", clan._id)
        )
    }
    
    
    fun setName(name: String, clan: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            set("name", name)
        )
    }
    
    fun setTag(tag: String,clan: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            set("tag", tag)
        )
    }
}