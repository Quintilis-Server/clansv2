package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.bukkit.entity.Enemy
import java.util.UUID

data class DeathEntity(
    @BsonId
    val id: ObjectId = ObjectId(),
    val killer: UUID,
    val victim: UUID,
    val isAlly: Boolean = false,
    val isEnemy: Boolean = false
)