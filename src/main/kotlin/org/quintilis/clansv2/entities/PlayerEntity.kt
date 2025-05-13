package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class PlayerEntity(
    @BsonId
    val id: UUID,
    val name: String,
    val points: Int = 0,
    val clanId: ObjectId? = null
)