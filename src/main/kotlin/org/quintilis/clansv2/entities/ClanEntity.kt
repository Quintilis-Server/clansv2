package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.UUID

data class ClanEntity @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id")    val _id: ObjectId,
    @param:BsonProperty("name") val name: String = "",                 // default
    @param:BsonProperty("tag") val tag: String? = null,
    @param:BsonProperty("members") val members: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("points") val points: Int = 0,
    @param:BsonProperty("allies") val allies: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("enemies") val enemies: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("owner") val owner: ObjectId? = null,
){
    override fun toString(): String{
        return "Name: $name, Tag: $tag, Owner: $owner, Enemies: $enemies, allies: $allies, points: $points"
    }
}