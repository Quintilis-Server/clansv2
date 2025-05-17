package org.quintilis.clansv2.commands.ally

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.CommandException
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.InviteManager

class AllyCommand(
    private val clanCollection: MongoCollection<ClanEntity>,
): CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, AllyCommands.entries.map { it.usage }.toTypedArray())
        }
        
        when(p3[0]){
            AllyCommands.LIST.command -> list(commandSender = p0)
            AllyCommands.ACCEPT.command -> accept(commandSender = p0, args = p3.sliceArray(1 until p3.size))
            AllyCommands.REJECT.command -> reject(commandSender = p0, args = p3.sliceArray(1 until p3.size))
            AllyCommands.SEND.command -> send(commandSender = p0, args = p3.sliceArray(1 until p3.size))
        }
        return true
    }
    fun list(commandSender: CommandSender){
        var out = "";
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            commandSender.sendMessage("Você não é dono de nenhum clã!")
            return
        }
        InviteManager.getAllyInvitesByReceiver(clan._id!!).forEach {
            out += "${ChatColor.YELLOW}${it.sender} ${ChatColor.GRAY}solicitou uma aliança com o clã ${ChatColor.YELLOW}${clanCollection.find(eq(it.sender)).first()?.name}" + "\n"
        }
        commandSender.sendMessage(out)
    }
    
    fun accept(commandSender: CommandSender, args: Array<String>) {
        val clanId = ClanManager.getClanByName(args[0])!!._id
        val invite = InviteManager.getAllyInvitesBySender(clanId)
        if(invite == null) {
            throw org.bukkit.command.CommandException("Você não tem nenhum convite para este clã")
        }
        InviteManager.acceptAllyInvite(invite)
        commandSender.sendMessage("Convite aceito")
    }
    
    fun reject(commandSender: CommandSender, args: Array<String>) {
        val clanId = ClanManager.getClanByName(args[0])!!._id
        val invite = InviteManager.getAllyInvitesBySender(clanId)
        if(invite == null) {
            throw org.bukkit.command.CommandException("Você não tem nenhum convite para este clã")
        }
        InviteManager.rejectAllyInvite(invite)
        commandSender.sendMessage("Convite recusado")
    }
    
    fun send(commandSender: CommandSender, args: Array<String>) {
        val senderClan = ClanManager.getClanByOwner(commandSender as Player)
        if(senderClan == null) {
            throw org.bukkit.command.CommandException("Você não é dono de um clã")
        }
        
        val receiverClan = ClanManager.getClanByName(args[0])
        if(receiverClan == null) {
            throw org.bukkit.command.CommandException("Clã não encontrada")
        }
        
        val invite = InviteManager.getAllyInvitesBySender(receiverClan._id)
        if(invite != null) {
            throw org.bukkit.command.CommandException("Você já enviou um convite para esta clã")
        }
        
        InviteManager.addAllyInvite(sender = senderClan,receiver = receiverClan)
    }
    
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        if(p3.size == 1) {
            return AllyCommands.entries.map { it.command }
        }else if (p3.size == 2) {
            return when(p3[0]) {
                AllyCommands.LIST.command -> listOf()
                AllyCommands.ACCEPT.command -> listAllyInvites(p0 as Player)
                AllyCommands.REJECT.command -> listAllyInvites(p0 as Player)
                else -> return listOf()
            }
        }
        
        return null
    }
    
    private fun listAllyInvites(player: Player): List<String> {
        val receiverClan = clanCollection.find(eq("owner",player.uniqueId)).first()
        val invites = InviteManager.getAllyInvitesByReceiver(receiverClan._id!!)
        return invites.map {
            clanCollection.find(eq(it.sender)).first()!!.name
        }
    }
    
}