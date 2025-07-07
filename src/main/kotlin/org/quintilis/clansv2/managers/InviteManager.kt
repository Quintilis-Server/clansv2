package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.Invite
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color
import org.quintilis.clansv2.string.italic
import java.util.Date

object InviteManager {
    private val playerInvites = MongoManager.playerInviteCollection
    
    private var playerExpirationHours: Int = 1;
    
    fun setConfig( playerExpirationHours: Int?) {
        this.playerExpirationHours = playerExpirationHours?:1
    }
    
    //player invite
    
    fun getPlayerInvites() = playerInvites.find().toList()
    
    fun addPlayerInvite(sender: PlayerEntity, receiver: PlayerEntity, clan: ClanEntity) {
        val invite = Invite(
            sender = sender._id,
            receiver = receiver._id,
            clan = clan._id,
            expireDate = Date(System.currentTimeMillis() + (playerExpirationHours * 60 * 60 * 1000))
        )
        try{
            playerInvites.insertOne(invite)
        }catch (e: Exception){
            throw e
        }
        
        Bukkit.getPlayer(receiver.mineId)?.sendMessage("O clã ${clan.name} quer te convidar para se unir. use:" + "\"/invite accept ${clan.name}\"".bold().color(
            ChatColor.YELLOW) + " para aceitar.")
        playerInvites.insertOne(invite)
    }
    
    fun getPlayerInvitesByReceiver(receiver: PlayerEntity): List<Invite> {
        return playerInvites.find(eq("receiver", receiver._id)).toList()
    }
    
    fun getPlayerInviteBySender(sender: ObjectId): Invite? {
        return playerInvites.find(eq("sender", sender)).first()
    }
    
    fun acceptInvite(receiver: Player, clan: ClanEntity) {
        val receiverEntity: PlayerEntity = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
        ClanManager.addMember(clan, receiverEntity)
        ClanManager.sendMessageToMembers(clan, "O jogador ${receiver.name.bold()} entrou para o clã")
//        playerInvites.deleteOne(and(eq("receiver", receiverEntity._id), eq("clan", clan._id)))
        playerInvites.updateOne(
            and(
                eq("receiver", receiverEntity._id),
                eq("clan", clan._id),
                eq("sender", clan.owner),
                eq("active",true)
            ),
            set("active", false),
        )
        playerInvites.updateOne(
            and(
                eq("receiver", receiverEntity._id),
                eq("clan", clan._id),
                eq("sender", clan.owner),
                eq("active", true),
            ),
            set("accepted", true),
        )
    }
    fun rejectInvite(receiver: Player, clan: ClanEntity) {
        val owner = PlayerManager.getPlayerById(clan.owner)!!
        val receiverEntity: PlayerEntity = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
        Bukkit.getPlayer(owner.mineId)?.sendMessage("A sua solicitação de entrar no clã ${clan.name} foi " + "recusada.".color(ChatColor.RED).italic())
        
        playerInvites.updateOne(
            and(
                eq("receiver", receiverEntity._id),
                eq("clan", clan._id),
                eq("sender", clan.owner),
                eq("active", true),
            ),
            set("active", false)
        )
    }
}