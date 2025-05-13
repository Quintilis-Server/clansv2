package org.quintilis.clansv2.managers

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.DeathEntity
import org.quintilis.clansv2.entities.PlayerEntity

object MongoManager {
    lateinit var mongoClient: MongoClient
    lateinit var database: MongoDatabase
    lateinit var clanCollection: MongoCollection<ClanEntity>
    lateinit var playerCollection: MongoCollection<PlayerEntity>
    lateinit var deathCollection: MongoCollection<DeathEntity>
    
    fun connect(uri: String, dbName: String = "minecraft"){
        mongoClient = MongoClients.create(uri)
        database = this.mongoClient.getDatabase(dbName)
        clanCollection = database.getCollection("clans", ClanEntity::class.java)
        playerCollection = database.getCollection("players", PlayerEntity::class.java)
        deathCollection = database.getCollection("deaths", DeathEntity::class.java)
    }
    
    fun close() {
        mongoClient.close()
    }
}