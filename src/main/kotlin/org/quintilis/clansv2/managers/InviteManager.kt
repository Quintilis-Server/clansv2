package org.quintilis.clansv2.managers

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
import java.util.UUID

object InviteManager {
    private val playerInvites = mutableListOf<Invite>()
    private val alliesInvites = mutableListOf<AllyInvite>()
    
    private var allyExpirationHours: Int = 2;
    private var playerExpirationHours: Int = 1;
    
    fun setConfig(allyExpirationHours: Int?, playerExpirationHours: Int?) {
        this.allyExpirationHours = allyExpirationHours?:2
        this.playerExpirationHours = playerExpirationHours?:1
    }
    
    //player invite
    
    fun getPlayerInvites() = playerInvites.toList()
    
    fun addPlayerInvite(sender: Player, receiver: Player, clan: ClanEntity) {
        val invite = Invite(
            sender = sender.uniqueId,
            receiver = receiver.uniqueId,
            clan = clan._id,
            expireDate = Date(System.currentTimeMillis() + (playerExpirationHours * 60 * 60 * 1000))
        )
        
        receiver.sendMessage("O clã ${clan.name} quer te convidar para se unir. use:" + "\"/invite accept ${clan.name}\"".bold().color(
            ChatColor.YELLOW) + " para aceitar.")
        playerInvites.add(invite)
    }
    
    fun getPlayerInvitesByReceiver(receiver: UUID): List<Invite> {
        return playerInvites.filter { it.receiver == receiver }
    }
    
    fun getPlayerInviteBySender(sender: UUID): Invite? {
        return playerInvites.find { it.sender == sender }
    }
    
    fun acceptInvite(sender: Player, receiver: ClanEntity) {
        val senderEntity: PlayerEntity = PlayerManager.getPlayerByMineId(sender.uniqueId)!!
        ClanManager.addMember(receiver, senderEntity)
        ClanManager.sendMessageToMembers(receiver, "O jogador ${sender.name.bold()} entrou para o clã")
    }
    fun rejectInvite(sender: Player, receiver: ClanEntity) {
        val owner = PlayerManager.getPlayerById(receiver.owner)!!
        Bukkit.getPlayer(owner.mineId)?.sendMessage("A sua solicitação de entrar no clã ${receiver.name} foi " + "recusada.".color(ChatColor.RED).italic())
        playerInvites.removeIf { it.sender == sender.uniqueId && it.receiver == owner.mineId && it.clan == receiver._id }
    }
    
    //ally invite
    
    fun getAllyInvites() = alliesInvites.toList()
    
    fun addAllyInvite(receiver: ClanEntity, sender: ClanEntity) {
        val invite = AllyInvite(
            sender._id, receiver._id, expireTime = Date(System.currentTimeMillis() + (allyExpirationHours * 60 * 60 * 1000)),
        )
        val receiverOwner = Bukkit.getPlayer(PlayerManager.getUUID(receiver.owner))
        alliesInvites.add(invite)
        receiverOwner?.sendMessage("O clã ${sender.name} quer se aliar com você. use: \"/ally accept ${sender.name}\" para aceitar.")
    }
    
    fun getAllyInvitesByReceiver(receiver: ObjectId): List<AllyInvite> {
        return alliesInvites.filter { it.receiver == receiver }
    }
    
    fun getAllyInvitesBySender(sender: ObjectId?): AllyInvite? {
        return alliesInvites.find { it.sender == sender }
    }
    
    
    fun acceptAllyInvite(invite: AllyInvite) {
        
        val clanSender = ClanManager.getClanById(invite.sender!!)
        val clanReceiver = ClanManager.getClanById(invite.receiver!!)
        
        ClanManager.addAlly(clanSender!!, clanReceiver!!)
        
//        clanCollection.findOneAndUpdate(eq("id", invite.sender), push("allies", invite.receiver))
//        clanCollection.findOneAndUpdate(eq("id", invite.receiver), push("allies", invite.sender))
        
        for(member in clanSender!!.members) {
            Bukkit.getPlayer(PlayerManager.getPlayerById(member)?.mineId!!)?.sendMessage("O seu clã se aliou ${clanReceiver!!.name}.")
        }
        for(member in clanReceiver!!.members) {
            Bukkit.getPlayer(PlayerManager.getPlayerById(member)?.mineId!!)?.sendMessage("O seu clã se aliou ${clanSender.name}.")
        }
        
        alliesInvites.removeIf { it.sender == invite.sender && it.receiver == invite.receiver }
    }
    
    fun rejectAllyInvite(invite: AllyInvite) {
        val clanSender = ClanManager.getClanById(invite.sender!!)
        Bukkit.getPlayer(PlayerManager.getUUID(clanSender!!.owner))?.sendMessage("A sua solicitação de aliação foi recusada.")
        alliesInvites.removeIf { it.sender == invite.sender && it.receiver == invite.receiver }
    }
    
}