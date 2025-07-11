package org.quintilis.clansv2.managers

import org.quintilis.clansv2.entities.DeathEntity
import com.mongodb.client.model.Filters.*


object DeathManager {
    private val death = MongoManager.deathCollection
    
    fun saveReplace(deathEntity: DeathEntity){
        this.death.replaceOne(eq(deathEntity._id), deathEntity)
    }
    
    fun save(deathEntity: DeathEntity){
        this.death.insertOne(deathEntity)
    }
}