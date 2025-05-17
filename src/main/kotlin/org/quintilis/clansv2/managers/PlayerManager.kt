package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId
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
}