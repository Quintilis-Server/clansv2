package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.PlayerManager
import java.util.UUID

data class PlayerEntity @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id")      val _id: ObjectId,
    @param:BsonProperty("mineId")                 val mineId: UUID,
    @param:BsonProperty("name")                   val name: String,
    @param:BsonProperty("points")                 private var points: Int = 0,
    @param:BsonProperty("clanId")                 val clanId: ObjectId? = null,
){
    override fun toString(): String {
        return "_id: $_id, mineId: $mineId, name: $name, points: $points, clanId: $clanId"
    }
    
    fun save(){
        PlayerManager.saveReplace(this)
    }
    
    fun getPoints() : Int = points
    
    fun addPoints(points: Int) {
        this.points += points;
        this.save()
    }
    
    fun removePoints(points: Int) {
        this.points -= points;
        this.save()
    }
}