package org.quintilis.clansv2.commands.ally

import org.bukkit.Bukkit
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
import org.quintilis.clansv2.managers.PlayerManager

class AllyCommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, AllyCommands.entries.map { it.usage }.toTypedArray())
        }
        
        when(p3[0]){
            AllyCommands.LIST.command -> list(commandSender = p0, args = p3.sliceArray(1 until p3.size))
            AllyCommands.REMOVE.command -> remove(commandSender = p0, args = p3.sliceArray(1 until p3.size))
            AllyCommands.INVITE.command ->{
                when(p3[1]){
                    AllyInviteSubCommands.ACCEPT.command -> accept(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                    AllyInviteSubCommands.REJECT.command -> reject(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                    AllyInviteSubCommands.SEND.command -> send(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                }
            }
        }
        return true
    }
    
    
    private fun list(commandSender: CommandSender, args: Array<String>){
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        if(args.isEmpty()) {
            CommandException.notEnoughArgs(commandSender,min = 1,args = args)
            return
        }
        val list:List<String?> = when(args[0]){
            AllyListSubCommands.ALLIES.command -> listAllAllies(commandSender).map {
                "Clã: $it"
            }
            AllyListSubCommands.INVITES.command -> InviteManager.getAllyInvitesByReceiver(clan._id).map {
                "${ChatColor.YELLOW}${ClanManager.getClanById(it.sender!!)!!.name} ${ChatColor.GRAY}solicitou uma aliança com o clã ${ChatColor.YELLOW}${ClanManager.getClanById(it.receiver!!)!!.name}"
            }
            else -> {
                CommandException.sendAllUsage(commandSender, AllyListSubCommands.entries.map { it.usage }.toTypedArray())
                return
            }
        }
        commandSender.sendMessage(list.joinToString("\n"))
    }
    
    private fun accept(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(AllyInviteSubCommands.ACCEPT.usage))
            return
        }
        val clanId = ClanManager.getClanByName(args[0])!!._id
        val invite = InviteManager.getAllyInvitesBySender(clanId)
        if(invite == null) {
            return commandSender.sendMessage("Você não tem nenhum convite para este clã")
        }
        InviteManager.acceptAllyInvite(invite)
        commandSender.sendMessage("Convite aceito")
    }
    
    private fun reject(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(AllyInviteSubCommands.REJECT.usage))
            return
        }
        val clanId = ClanManager.getClanByName(args[0])!!._id
        val invite = InviteManager.getAllyInvitesBySender(clanId)
        if(invite == null) {
            throw org.bukkit.command.CommandException("Você não tem nenhum convite para este clã")
        }
        InviteManager.rejectAllyInvite(invite)
        commandSender.sendMessage("Convite recusado")
    }
    
    private fun send(commandSender: CommandSender, args: Array<String>) {
        val senderClan = ClanManager.getClanByOwner(commandSender as Player)
        if(senderClan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(AllyInviteSubCommands.SEND.usage))
            return
        }
        
        val receiverClan = ClanManager.getClanByName(args[0])
        if(receiverClan == null) {
            return commandSender.sendMessage("Clã não encontrada")
        }
        
        if(senderClan.allies.find { it == receiverClan._id } != null || receiverClan.allies.find { it == senderClan._id } != null) {
            return commandSender.sendMessage("Os clãs ja são aliados")
        }
        
        val invite = InviteManager.getAllyInvitesBySender(receiverClan._id)
        if(invite != null) {
            return commandSender.sendMessage("Você já enviou um convite para esta clã")
        }
        
        InviteManager.addAllyInvite(sender = senderClan,receiver = receiverClan)
        commandSender.sendMessage("Convite para clã ${receiverClan.name} enviado")
    }
    
    private fun remove(commandSender: CommandSender, args: Array<String>) {
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(AllyCommands.REMOVE.usage))
            return
        }
        
        val receiverClan = ClanManager.getClanByName(args[0])
        if(receiverClan == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        val receiverPlayer = Bukkit.getPlayer(PlayerManager.getPlayerById(receiverClan.owner)!!.mineId)!!
        
        commandSender.sendMessage("Sua aliança com o clã ${ChatColor.YELLOW}${receiverClan.name}${ChatColor.RESET} acabou")
        receiverPlayer.sendMessage("Sua aliança com o clã ${ChatColor.YELLOW}${clan.name}${ChatColor.RESET} acabou por conta do ${PlayerManager.getPlayerById(clan.owner)?.name}")
        
        ClanManager.removeAlly(clan,receiverClan)
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        return when (p3.size) {
            1 -> AllyCommands.entries.map { it.command }
            2 -> when (p3[0]?.lowercase()) {
//                AllyCommands.ACCEPT.command -> listAllyInvites(p0 as Player)
//                AllyCommands.REJECT.command -> listAllyInvites(p0 as Player)
//                AllyCommands.SEND.command -> listAllClans(p0 as Player)
                AllyCommands.INVITE.command -> AllyInviteSubCommands.entries.map { it.command }
                AllyCommands.LIST.command -> AllyListSubCommands.entries.map { it.command }
                AllyCommands.REMOVE.command -> listAllAllies(p0 as Player)
                else -> emptyList()
            }
            3 -> when(p3[1]?.lowercase()){
                AllyInviteSubCommands.ACCEPT.command -> listAllyInvites(p0 as Player)
                AllyInviteSubCommands.REJECT.command -> listAllyInvites(p0 as Player)
                AllyInviteSubCommands.SEND.command -> listAllClans(p0 as Player)
                else -> emptyList()
            }
            else -> emptyList()
        }
//        if(p3.size == 1) {
//            return AllyCommands.entries.map { it.command }
//        }else if (p3.size == 2) {
//            return when(p3[0]) {
//                AllyCommands.LIST.command -> listOf()
//                AllyCommands.ACCEPT.command -> listAllyInvites(p0 as Player)
//                AllyCommands.REJECT.command -> listAllyInvites(p0 as Player)
//                else -> return listOf()
//            }
//        }
//
//        return null
    }
    
    private fun listAllyInvites(player: Player): List<String> {
        val receiverClan = ClanManager.getClanByOwner(player)!!
        println(receiverClan._id)
        val invites = InviteManager.getAllyInvitesByReceiver(receiverClan._id)
        return invites.map {
            ClanManager.getClanById(it.sender!!)!!.name
        }
    }
    
    private fun listAllClans(player: Player): List<String> {
        val receiverClan = ClanManager.getClanByOwner(player)!!
        val clans = ClanManager.getAllClans().filterNot { it._id == receiverClan._id }
        return clans.map { it.name }
    }
    
    private fun listAllAllies(player: Player): List<String> {
        val receiverClan = ClanManager.getClanByOwner(player)!!
        val clans: List<ClanEntity> = receiverClan.allies.map { ClanManager.getClanById(it)!! }
        return clans.map { it.name }
    }
}