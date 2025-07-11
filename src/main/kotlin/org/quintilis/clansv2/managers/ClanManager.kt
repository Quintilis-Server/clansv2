package org.quintilis.clansv2.managers

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

object ClanManager {
    private val clan: MongoCollection<ClanEntity> = MongoManager.clanCollection;
    private val playerManager: PlayerManager = PlayerManager;
    private val invites: InviteManager = InviteManager
    
    fun create(clan: ClanEntity, owner: Player) {
        clan.members.add(clan.owner!!)
        PlayerManager.setClan(owner.uniqueId, clan)
        this.clan.insertOne(clan)
    }
    
    fun delete(clan: ClanEntity){
        clan.members.forEach {
            playerManager.setClan(it, null)
        }
        sendMessageToMembers(clan, "Clã esta sendo desfeito por ${PlayerManager.getPlayerById(clan.owner)?.name!!.bold()}".color(
            ChatColor.RED))
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
    fun getClanByMember(member: Player):  ClanEntity?{
        val playerEntity = PlayerManager.getPlayerEntityByPlayer(member)!!
        val clan  = this.clan.find(eq("members", playerEntity._id))
        return clan.first()
    }
    fun getAllClans(): List<ClanEntity> {
        return this.clan.find().toList()
    }
    
    fun exists(clanName: String): Boolean {
        return this.clan.find(eq("name", clanName)).first() != null;
    }
    
    fun isOwner(player: Player): Boolean {
        val playerEntity = PlayerManager.getPlayerByMineId(player.uniqueId)!!
        return this.clan.find(eq("owner", playerEntity._id)).first() != null;
    }
    
    fun isOwner(player: Player, clanId: ObjectId): Boolean {
        val clan = this.getClanById(clanId)
        return player.uniqueId == clan?.owner
    }
    
    fun isInClan(player: Player): Boolean {
        val playerEntity = playerManager.getPlayerEntityByPlayer(player) ?: return false
        return this.clan.find(eq("members", playerEntity._id)).first() != null
    }
    
//    fun isInClan(player: Player){
//
//    }
    
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
    fun addMember(clan: ClanEntity, member: PlayerEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            push("members", member._id)
        )
    }
    
    
    fun removeAlly(clan: ClanEntity, ally: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            pull("allies", ally._id)
        )
        this.clan.updateOne(
            eq("_id", ally._id),
            pull("allies", clan._id)
        )
    }
    
    
    fun removeMember(clan: ClanEntity, member: PlayerEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            pull("members", member._id)
        )
        this.playerManager.setClan(member._id, null)
    }
    
    fun removeMember(member: Player){
        val playerEntity = PlayerManager.getPlayerEntityByPlayer(member) ?: throw Exception("Erro")
        this.clan.updateOne(
            eq("_id", playerEntity._id),
            pull("members", playerEntity._id)
        )
        this.playerManager.setClan(playerEntity._id, null)
    }
    
    fun removeEnemy(clan: ClanEntity, clanSender: ClanEntity) {
        this.clan.updateOne(
            eq("_id", clan._id),
            pull("enemies", clanSender._id)
        )
        this.clan.updateOne(
            eq("_id", clanSender._id),
            pull("enemies", clan._id)
        )
    }
    
    fun isAlly(clan: ClanEntity, ally: ClanEntity):Boolean {
        val clanDoc =  this.clan.find(eq("_id", clan._id)).first()
        val allyDoc = this.clan.find(eq("_id", ally._id)).first()
        
        if (clanDoc == null || allyDoc == null) return false
        
        return ally._id in clanDoc.allies && clan._id in allyDoc.allies
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
    
    fun save(clan: ClanEntity) {
        this.clan.replaceOne(eq("_id", clan._id), clan)
    }
    
    fun updateClanPoints(clan: ClanEntity){
        var sum = 0;
        for(member in clan.members){
            val memberEntity = PlayerManager.getPlayerById(member)
            sum += memberEntity?.getPoints()!!
        }
        clan.points = sum
        clan.save()
    }
}