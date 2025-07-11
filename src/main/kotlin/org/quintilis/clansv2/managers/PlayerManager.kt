package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import java.util.UUID

object PlayerManager {
    private val player = MongoManager.playerCollection
    
    fun getUUID(id: ObjectId?): UUID {
        val player = this.getPlayerById(id!!)!!
        return player.mineId
    }
    
    fun getPlayerByMineId(mineId: UUID): PlayerEntity? {
        return this.player.find(eq("mineId", mineId)).first()
    }
    
    fun getPlayerById(id: ObjectId): PlayerEntity? {
        return this.player.find(eq("_id", id)).first()
    }
    
    fun getPlayerByName(name: String): PlayerEntity? {
        return this.player.find(eq("name", name)).first()
    }
    
    fun setClan(id: UUID, clan: ClanEntity) {
        println(id.toString())
        if (this.exists(id)) {
            this.player.updateOne(
                eq("mineId", id),
                set("clanId", clan._id)
            )
        }
    }
    
    fun setClan(id: ObjectId, clan: ClanEntity?) {
        if(this.exists(id)) {
            this.player.updateOne(
                eq("_id", id),
                set("clanId", clan?._id)
            )
        }
    }
    
    fun exists(id: ObjectId): Boolean {
        return this.player.find(eq("_id", id)).first() != null
    }
    fun exists(mineId: UUID): Boolean {
        return this.player.find(eq("mineId", mineId)).first() != null
    }
    
    fun saveReplace(entity: PlayerEntity) {
        this.player.replaceOne(eq(entity._id),entity)
    }
    
    private fun save(entity: PlayerEntity): PlayerEntity {
        this.player.insertOne(entity)
        return entity
    }
    
    fun getPlayerEntityByPlayer(player: Player): PlayerEntity?{
        return this.player.find(eq("mineId", player.uniqueId)).first()
    }
    
    fun saveIfNotExists(entity: Player): PlayerEntity? {
        if (!exists(entity.uniqueId)) {
            Bukkit.getLogger().info("Colocando player na database")
            val player = PlayerEntity(mineId = entity.uniqueId, name = entity.name, _id = ObjectId())
            return this.save(player)
        }else{
            Bukkit.getLogger().info("Player ja está na database")
            return null
        }
    }
}