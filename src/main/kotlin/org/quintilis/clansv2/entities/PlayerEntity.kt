package org.quintilis.clansv2.entities

import org.bson.types.ObjectId
import java.util.UUID

data class PlayerEntity(
    var _id: ObjectId = ObjectId(),
    var mineId: UUID = UUID.randomUUID(),
    var name: String = "",
    var points: Int = 0,
    var clanId: ObjectId? = null
)