package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.AllyInviteManager
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.string.bold
import java.util.Date

data class AllyInvite @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id") val _id: ObjectId = ObjectId(),
    //Clan id
    @param:BsonProperty("sender") val sender: ObjectId,
    //Clan id
    @param:BsonProperty("receiver") val receiver: ObjectId,
    
    @param:BsonProperty("sendDate") val sendDate: Date = Date(System.currentTimeMillis()),
    
    @param:BsonProperty("expireDate") val expireDate: Date = Date(Date().time + AllyInviteManager.getAllyExpirationHours() * 360000 ),
    
    @param:BsonProperty("accepted") var accepted: Boolean = false,
    
    @param:BsonProperty("active") var active: Boolean = true,
){
    fun showInfo(): String {
        return "Sender: ${ClanManager.getClanById(sender)!!.name.bold()} Data enviada: ${sendDate.toString().bold()}, Data de expiração: ${expireDate.toString().bold()}"
    }
    override fun toString(): String {
        return "[sender: $sender, receiver: $receiver, Data enviada: $sendDate, Data de expiração: $expireDate],\n"
    }
    
    fun accept() {
        val sender = ClanManager.getClanById(sender);
        val receiver = ClanManager.getClanById(receiver);
        if(sender != null && receiver != null && expireDate > Date()) {
            ClanManager.addAlly(sender, receiver);
            this.accepted = true;
            this.active = false;
            this.save()
            return
        }
        throw Error("aaaa")
    }
    
    fun reject(){
        val sender = ClanManager.getClanById(sender);
        val receiver = ClanManager.getClanById(receiver);
        if(sender != null && receiver != null) {
            this.accepted = false;
            this.active = false;
            this.save()
        }
    }
    
    fun save() {
        return AllyInviteManager.save(this)
    }
}