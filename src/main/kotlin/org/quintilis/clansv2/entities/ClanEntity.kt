package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class ClanEntity(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val members: MutableList<UUID> = mutableListOf(),
    val points: Int = 0,
    val allies: MutableList<ObjectId> = mutableListOf(),
    val enemies: MutableList<ObjectId> = mutableListOf()
)