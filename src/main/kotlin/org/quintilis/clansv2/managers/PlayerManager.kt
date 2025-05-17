package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.PlayerEntity
import java.util.UUID

object PlayerManager {
    private val player = MongoManager.playerCollection
    
    fun getUUID(id: ObjectId): UUID {
        val player = this.getPlayerById(id)!!
        return player.mineId
    }
    
    fun getPlayerByMineId(mineId: UUID): PlayerEntity? {
        return this.player.find(eq("mineId", mineId)).first()
    }
    
    fun getPlayerById(id: ObjectId): PlayerEntity? {
        return this.player.find(eq("_id", id)).first()
    }
    
    fun exists(id: ObjectId): Boolean {
        return this.player.find(eq("_id", id)).first() != null
    }
    fun exists(mineId: UUID): Boolean {
        return this.player.find(eq("mineId", mineId)).first() != null
    }
    
    fun save(entity: PlayerEntity): PlayerEntity {
        this.player.insertOne(entity)
        return entity
    }
    
    fun saveIfNotExists(entity: PlayerEntity) {
        if (exists(entity._id)) {
            entity
        }else{
            this.save(entity)
        }
    }
    fun saveIfNotExists(entity: Player) {
        if (!exists(entity.uniqueId)) {
            Bukkit.getLogger().info("Colocando player na database")
            val player = PlayerEntity(mineId = entity.uniqueId, name = entity.name)
            this.save(player)
        }else{
            Bukkit.getLogger().info("Player ja está na database")
        }
    }
}