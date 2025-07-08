package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.*
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.Invite
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.string.*
import java.util.Date

object InviteManager {
    private val playerInvites = MongoManager.playerInviteCollection
    
    var playerExpirationHours: Int = 1;
    
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
    
    fun getPlayerInviteByReceiverAndClan(receiver: PlayerEntity, clan: ClanEntity): Invite? {
        return playerInvites.find(
            and(
                eq("receiver", receiver._id),
                eq("clan", clan._id),
                gte("expireDate", Date())
            )
        ).firstOrNull()
    }
    
    fun getPlayerInvitesByReceiver(receiver: PlayerEntity): List<Invite> {
        return playerInvites.find(
            and(
            eq("receiver", receiver._id),
                gte("expireDate", Date())
            )
        ).toList()
    }
    
    fun getPlayerInvitesBySender(sender: ObjectId): Invite? {
        return playerInvites.find(
            and(
            eq("sender", sender),
                gte("expireDate", Date()),
            )
        ).first()
    }
    
    fun acceptInvite(receiver: Player, clan: ClanEntity) {
        val receiverEntity: PlayerEntity = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
        val invite = this.getPlayerInviteByReceiverAndClan(receiverEntity, clan)
        if(invite == null) {
            receiver.sendMessage("Convite para o clã ${clan.name.bold()} não encontrado")
            return
        }
        invite.accept()
//        receiver.sendMessage("Você entrou para o clã ${clan.name.bold()}".color(ChatColor.YELLOW))
        ClanManager.sendMessageToMembers(clan, "O jogador ${receiver.name.bold()} entrou para o clã")
//        ClanManager.addMember(clan, receiverEntity)
////        playerInvites.deleteOne(and(eq("receiver", receiverEntity._id), eq("clan", clan._id)))
//        playerInvites.updateOne(
//            and(
//                eq("receiver", receiverEntity._id),
//                eq("clan", clan._id),
//                eq("sender", clan.owner),
//                eq("active",true)
//            ),
//            set("active", false),
//        )
//        playerInvites.updateOne(
//            and(
//                eq("receiver", receiverEntity._id),
//                eq("clan", clan._id),
//                eq("sender", clan.owner),
//                eq("active", true),
//            ),
//            set("accepted", true),
//        )
    
    }
    fun rejectInvite(receiver: Player, clan: ClanEntity) {
        val receiverEntity: PlayerEntity = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
        val invite = this.getPlayerInviteByReceiverAndClan(receiverEntity, clan)
        val owner = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
        if(invite == null) {
            receiver.sendMessage("Convite para o clã ${clan.name.bold()} não encontrado")
            return
        }
        invite.reject()
        Bukkit.getPlayer(owner.mineId)?.sendMessage("A sua solicitação de entrar no clã ${clan.name} foi " + "recusada.".color(ChatColor.RED).italic())
//        receiver.sendMessage("Convite recusado".color(ChatColor.RED).bold())
        
//        val owner = PlayerManager.getPlayerById(clan.owner)!!
//        val receiverEntity: PlayerEntity = PlayerManager.getPlayerByMineId(receiver.uniqueId)!!
//
//        playerInvites.updateOne(
//            and(
//                eq("receiver", receiverEntity._id),
//                eq("clan", clan._id),
//                eq("sender", clan.owner),
//                eq("active", true),
//            ),
//            set("active", false)
//        )
    }
    
    fun save(invite: Invite) {
        this.playerInvites.replaceOne(eq(invite._id), invite)
    }
}