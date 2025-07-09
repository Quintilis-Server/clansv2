package org.quintilis.clansv2.managers

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.DeathEntity
import org.quintilis.clansv2.entities.PlayerEntity

import org.bson.codecs.configuration.CodecRegistries.*
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.Invite
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

object MongoManager {
    lateinit var mongoClient: MongoClient
    lateinit var database: MongoDatabase
    lateinit var clanCollection: MongoCollection<ClanEntity>
    lateinit var playerCollection: MongoCollection<PlayerEntity>
    lateinit var deathCollection: MongoCollection<DeathEntity>
    lateinit var playerInviteCollection: MongoCollection<Invite>
    lateinit var allyInviteCollection: MongoCollection<AllyInvite>
    
    private val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder()
            .register("org.quintilis.clansv2.entities")
            .automatic(true)
            .build()
        ),
    )
    
    
    fun connect(uri: String, dbName: String = "minecraft"):Boolean{
        return try{
            val connString = ConnectionString(uri)
            val settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .uuidRepresentation(UuidRepresentation.STANDARD)// ← aqui aplica sua URI :contentReference[oaicite:0]{index=0}
                .codecRegistry(pojoCodecRegistry)                     // ← registra codecs para POJOs
                .build()
            mongoClient = MongoClients.create(settings)
            database = this.mongoClient.getDatabase(dbName)
            database.runCommand(Document("ping", 1))
            
            clanCollection = database.getCollection("clans", ClanEntity::class.java)
            playerCollection = database.getCollection("players", PlayerEntity::class.java)
            deathCollection = database.getCollection("deaths", DeathEntity::class.java)
            playerInviteCollection = database.getCollection("playerInvites", Invite::class.java)
            allyInviteCollection = database.getCollection("allyInvites", AllyInvite::class.java)
            true
        }catch (e: Exception){
            Bukkit.getLogger().severe("Erro ao conectar ao MongoDB: ${e.message}".color(ChatColor.RED).bold())
            false
        }
    }
    
    fun close() {
        mongoClient?.let {
            mongoClient.close()
        }
    }
    
    
}