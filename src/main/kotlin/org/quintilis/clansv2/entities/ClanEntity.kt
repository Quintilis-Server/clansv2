package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.UUID

data class ClanEntity(
    @BsonId
    var _id: ObjectId = ObjectId(),        // default garante no-args ctor
    
    var name: String = "",                 // default
    var tag: String? = null,
    var members: MutableList<ObjectId> = mutableListOf(),
    var points: Int = 0,
    var allies: MutableList<ObjectId> = mutableListOf(),
    var enemies: MutableList<ObjectId> = mutableListOf(),
    var owner: ObjectId = ObjectId()
){
    override fun toString(): String{
        return "Name: $name, Tag: $tag, Owner: $owner"
    }
}