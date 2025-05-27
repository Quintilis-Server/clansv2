package org.quintilis.clansv2.managers

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.AllyInvite
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.Invite
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color
import org.quintilis.clansv2.string.italic
import java.util.Date

object InviteManager {
    private val playerInvites = MongoManager.playerInviteCollection
    private val alliesInvites = MongoManager.allyInviteCollection
    
    private var allyExpirationHours: Int = 2;
    private var playerExpirationHours: Int = 1;
    
    fun setConfig(allyExpirationHours: Int?, playerExpirationHours: Int?) {
        this.allyExpirationHours = allyExpirationHours?:2
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
        
        Bukkit.getPlayer(receiver.mineId)?.sendMessage("O clã ${clan.name} quer te convidar para se unir. use:" + "\"/invite accept ${clan.name}\"".bold().color(
            ChatColor.YELLOW) + " para aceitar.")
        playerInvites.insertOne(invite)
    }
    
    fun getPlayerInvitesByReceiver(receiver: ObjectId): List<Invite> {
        return playerInvites.find(eq("receiver", receiver)).toList()
    }
    
    fun getPlayerInviteBySender(sender: ObjectId): Invite? {
        return playerInvites.find(eq("sender", sender)).first()
    }
    
    fun acceptInvite(sender: Player, clan: ClanEntity) {
        val senderEntity: PlayerEntity = PlayerManager.getPlayerByMineId(sender.uniqueId)!!
        ClanManager.addMember(clan, senderEntity)
        ClanManager.sendMessageToMembers(clan, "O jogador ${sender.name.bold()} entrou para o clã")
        playerInvites.deleteOne(and(eq("sender", senderEntity._id), eq("clan", clan._id)))
    }
    fun rejectInvite(sender: Player, clan: ClanEntity) {
        val owner = PlayerManager.getPlayerById(clan.owner)!!
        val senderEntity: PlayerEntity = PlayerManager.getPlayerByMineId(sender.uniqueId)!!
        Bukkit.getPlayer(owner.mineId)?.sendMessage("A sua solicitação de entrar no clã ${clan.name} foi " + "recusada.".color(ChatColor.RED).italic())
        playerInvites.deleteOne(and(eq("sender", senderEntity._id), eq("clan", clan._id)))
    }
    
    //ally invite
    
    fun getAllyInvites() = alliesInvites.find().toList()
    
    fun addAllyInvite(receiver: ClanEntity, sender: ClanEntity) {
        val invite = AllyInvite(
            sender = sender._id, receiver = receiver._id, expireDate = Date(System.currentTimeMillis() + (allyExpirationHours * 60 * 60 * 1000)),
        )
        val receiverOwner = Bukkit.getPlayer(PlayerManager.getUUID(receiver.owner))
        alliesInvites.insertOne(invite)
        receiverOwner?.sendMessage("O clã ${sender.name.bold()} quer se aliar com você. use: \"${"/ally invite accept ${sender.name}".bold()}\" para aceitar.")
    }
    
    fun getAllyInvitesByReceiver(receiver: ObjectId): List<AllyInvite> {
        return alliesInvites.find(eq("receiver", receiver)).toList()
    }
    
    fun getAllyInvitesBySender(sender: ObjectId?): List<AllyInvite?> {
        return alliesInvites.find(eq("sender", sender)).toList()
    }
    
    
    fun acceptAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        
//        val clanSender = ClanManager.getClanById(invite.sender!!)
//        val clanReceiver = ClanManager.getClanById(invite.receiver!!)
        
        ClanManager.addAlly(sender, receiver)
        
//        clanCollection.findOneAndUpdate(eq("id", invite.sender), push("allies", invite.receiver))
//        clanCollection.findOneAndUpdate(eq("id", invite.receiver), push("allies", invite.sender))
        
        for(member in sender.members) {
            Bukkit.getPlayer(PlayerManager.getPlayerById(member)?.mineId!!)?.sendMessage("O seu clã se aliou ${receiver.name}.")
        }
        for(member in receiver.members) {
            Bukkit.getPlayer(PlayerManager.getPlayerById(member)?.mineId!!)?.sendMessage("O seu clã se aliou ${sender.name}.")
        }
        
        alliesInvites.deleteOne(and(eq("sender", sender._id), eq("receiver", receiver._id)))
    }
    
    fun rejectAllyInvite(sender: ClanEntity, receiver: ClanEntity) {
        Bukkit.getPlayer(PlayerManager.getUUID(sender.owner))?.sendMessage("A sua solicitação de aliação foi recusada.")
        alliesInvites.deleteOne(and(eq("sender", sender._id), eq("receiver", receiver._id)))
    }
    
}