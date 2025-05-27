package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.Date

data class AllyInvite @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id") val _id: ObjectId = ObjectId(),
    //Clan id
    @param:BsonProperty("sender") val sender: ObjectId?,
    //Clan id
    @param:BsonProperty("receiver") val receiver: ObjectId?,
    
    @param:BsonProperty("sendDate") val sendDate: Date = Date(System.currentTimeMillis()),
    
    @param:BsonProperty("expireDate") val expireDate: Date
){
    override fun toString(): String {
        return "sender: $sender, receiver: $receiver, Data enviada: $sendDate, Data de expiração: $expireDate"
    }
}