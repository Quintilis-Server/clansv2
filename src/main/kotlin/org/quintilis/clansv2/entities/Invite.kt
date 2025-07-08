package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.InviteManager
import org.quintilis.clansv2.managers.PlayerManager
import org.quintilis.clansv2.string.bold
import java.util.Date

data class Invite @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id") val _id: ObjectId = ObjectId(),
    @param:BsonProperty("sender") val sender: ObjectId,
    @param:BsonProperty("receiver") val receiver: ObjectId,
    @param:BsonProperty("clan") val clan: ObjectId,
    @param:BsonProperty("sendDate") val sendDate: Date = Date(System.currentTimeMillis()),
    @param:BsonProperty("expireDate") val expireDate: Date = Date(Date().time + InviteManager.playerExpirationHours * 3600 * 1000),
    @param:BsonProperty("accepted") private var accepted: Boolean = false,
    @param:BsonProperty("active") private var active: Boolean = true,
) {
    override fun toString(): String {
        val sender = PlayerManager.getPlayerById(sender)!!
        val receiver = PlayerManager.getPlayerById(receiver)!!
        if(clan == null) return "Remetente: ${sender.name.bold()}, Destinatário: ${receiver.name.bold()}"
        val clan = ClanManager.getClanById(clan)!!
        return "Remetente: ${sender.name.bold()}, Destinatário: ${receiver.name.bold()}, Clã: ${clan.name.bold()}"
    }
    
    fun save(){
        InviteManager.save(this)
    }
    
    fun accept(){
        val clan = ClanManager.getClanById(clan)
        val player = PlayerManager.getPlayerById(receiver)
        if(clan == null||player == null) return
        ClanManager.addMember(clan, player)
        this.active = false;
        this.accepted = true;
        this.save();
    }
    
    fun reject(){
        this.active = false;
        this.accepted = false;
        this.save();
    }
}
