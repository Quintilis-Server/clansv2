package org.quintilis.clansv2.entities

import org.bson.types.ObjectId
import java.util.UUID

data class PlayerEntity(
    val _id: ObjectId? = ObjectId(),
    val mineId: UUID,
    val name: String,
    val points: Int = 0,
    val clanId: ObjectId? = null
)