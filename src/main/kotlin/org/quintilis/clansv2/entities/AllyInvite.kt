package org.quintilis.clansv2.entities

import org.bson.types.ObjectId

data class AllyInvite(
    //Clan id
    val sender: ObjectId,
    //Clan id
    val receiver: ObjectId,
)