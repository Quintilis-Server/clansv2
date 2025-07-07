package org.quintilis.clansv2.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.quintilis.clansv2.managers.ClanManager
import java.util.UUID

data class ClanEntity @BsonCreator constructor(
    @field:BsonId @param:BsonProperty("_id")    val _id: ObjectId,
    @param:BsonProperty("name") val name: String = "",                 // default
    @param:BsonProperty("tag") val tag: String? = null,
    @param:BsonProperty("members") val members: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("points") val points: Int = 0,
    @param:BsonProperty("allies") val allies: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("enemies") val enemies: MutableList<ObjectId> = mutableListOf(),
    @param:BsonProperty("owner") val owner: ObjectId,
){
    override fun toString(): String{
        return "Name: $name, Tag: $tag, Owner: $owner, Enemies: $enemies, allies: $allies, points: $points"
    }
    
    
    fun isAlliedWith(clan: ClanEntity): Boolean {
        return ClanManager.isAlly(this, clan)
    }
    
    fun ClanEntity.removeAlly(other: ClanEntity) {
        ClanManager.removeAlly(this, other)
    }
    
    fun ClanEntity.addEnemy(other: ClanEntity) {
        ClanManager.addEnemy(this, other)
    }
    
    fun ClanEntity.removeEnemy(other: ClanEntity) {
        ClanManager.removeEnemy(this, other)
    }
    
    fun ClanEntity.addMember(member: PlayerEntity) {
        ClanManager.addMember(this, member)
    }
    
    fun ClanEntity.removeMember(member: PlayerEntity) {
        ClanManager.removeMember(this, member)
    }
    
    fun ClanEntity.renameTo(name: String) {
        ClanManager.setName(name, this)
    }
    
    fun ClanEntity.setTag(tag: String) {
        ClanManager.setTag(tag, this)
    }
    
}