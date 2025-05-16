package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class ClanEntity(
    @BsonId val _id: ObjectId = ObjectId(),
    val name: String,
    val tag: String?,
    val members: MutableList<UUID> = mutableListOf(),
    val points: Int = 0,
    val allies: MutableList<ObjectId> = mutableListOf(),
    val enemies: MutableList<ObjectId> = mutableListOf(),
    val owner: UUID,
) {
    override fun toString(): String{
        return "Name: $name, Tag: $tag, Owner: $owner"
    }
}