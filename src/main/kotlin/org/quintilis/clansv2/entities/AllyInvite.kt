package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.string.bold
import java.util.Date

data class AllyInvite @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id") val _id: ObjectId = ObjectId(),
    //Clan id
    @param:BsonProperty("sender") val sender: ObjectId?,
    //Clan id
    @param:BsonProperty("receiver") val receiver: ObjectId?,
    
    @param:BsonProperty("sendDate") val sendDate: Date = Date(System.currentTimeMillis()),
    
    @param:BsonProperty("expireDate") val expireDate: Date,
    
    @param:BsonProperty("accepted") val accepted: Boolean = false,
    
    @param:BsonProperty("active") val active: Boolean = true,
){
    fun showInfo(): String {
        return "Sender: ${ClanManager.getClanById(sender!!)!!.name.bold()} Data enviada: ${sendDate.toString().bold()}, Data de expiração: ${expireDate.toString().bold()}"
    }
    override fun toString(): String {
        return "[sender: $sender, receiver: $receiver, Data enviada: $sendDate, Data de expiração: $expireDate],\n"
    }
}