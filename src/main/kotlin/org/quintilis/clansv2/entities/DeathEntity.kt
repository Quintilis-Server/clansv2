package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.bukkit.entity.Enemy
import java.util.UUID

data class DeathEntity(
    @field:BsonId @param:BsonProperty("_id") val _id: ObjectId = ObjectId(),
    @param:BsonProperty("killer") val killer: ObjectId,
    @param:BsonProperty("victim") val victim: ObjectId,
    @param:BsonProperty("isAlly") var isAlly: Boolean = false,
    @param:BsonProperty("isEnemy") var isEnemy: Boolean = false,
    @param:BsonProperty("sameClan") var sameClan: Boolean = false,
)