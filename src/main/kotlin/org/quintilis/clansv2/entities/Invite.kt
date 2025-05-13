package org.quintilis.clansv2.entities

import org.bson.types.ObjectId
import java.util.Date
import java.util.UUID

data class Invite(
    val sender: UUID,
    val receiver: UUID,
    val clan: ObjectId,
    val expireDate: Date = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
)
