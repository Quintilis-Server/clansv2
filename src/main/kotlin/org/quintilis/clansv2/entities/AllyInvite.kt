package org.quintilis.clansv2.entities

import org.bson.types.ObjectId
import java.util.Date

data class AllyInvite(
    //Clan id
    val sender: ObjectId?,
    //Clan id
    val receiver: ObjectId?,
    
    val sendTime: Date = Date(System.currentTimeMillis()),
    
    val expireTime: Date
){
    override fun toString(): String {
        return "sender: $sender, receiver: $receiver"
    }
}