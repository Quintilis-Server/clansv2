package org.quintilis.clansv2.entities

import org.bson.types.ObjectId

data class AllyInvite(
    val sender: ObjectId,
    val receiver: ObjectId,
)