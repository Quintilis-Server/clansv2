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
import org.quintilis.clansv2.managers.AllyInviteManager
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.PlayerManager
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

class AllyCommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, AllyCommands.entries.toTypedArray())
        }
        
        when(p3[0]){
            AllyCommands.LIST.command -> list(commandSender = p0)
            AllyCommands.REMOVE.command -> remove(commandSender = p0, args = p3.sliceArray(1 until p3.size))
            AllyCommands.INVITE.command ->{
                val args = p3.sliceArray(1 until p3.size)
                if(args.isEmpty()){
                    return CommandException.sendAllUsage(p0, AllyInviteSubCommands.entries.toTypedArray())
                }
                when(p3[1]){
                    AllyInviteSubCommands.ACCEPT.command -> accept(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                    AllyInviteSubCommands.REJECT.command -> reject(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                    AllyInviteSubCommands.SEND.command -> send(commandSender = p0, args = p3.sliceArray(2 until p3.size))
                    AllyInviteSubCommands.LIST.command -> listInvites(commandSender = p0)
                    else -> CommandException.sendAllUsage(p0, AllyInviteSubCommands.entries.toTypedArray())
                }
            }
        }
        return true
    }
    
    
    private fun list(commandSender: CommandSender){
        
        val clan =
            if(ClanManager.isOwner(commandSender as Player))
                ClanManager.getClanByOwner(commandSender)
            else
                ClanManager.getClanByMember(commandSender)
        if(clan == null){
            CommandException.notInAClan(commandSender)
            return
        }
        
        val allies = clan.allies.map { ClanManager.getClanById(it)!! }
        commandSender.sendMessage("Alianças (${allies.size}):".color(ChatColor.YELLOW))
        commandSender.sendMessage(allies.joinToString(", \n") { it.name })
        
    }
    
    private fun listInvites(commandSender: CommandSender){
        val clan =
            if(ClanManager.isOwner(commandSender as Player))
                ClanManager.getClanByOwner(commandSender)
            else
                ClanManager.getClanByMember(commandSender)
        if(clan == null){
            CommandException.notInAClan(commandSender)
            return
        }
        val invites = AllyInviteManager.getAllyInvitesByReceiver(clan)
        println(invites.map { it })
        commandSender.sendMessage("Invites de amizade (${invites.size}):".color(ChatColor.YELLOW))
        commandSender.sendMessage(invites.joinToString(", \n") { it.showInfo() })
    }
    
    private fun accept(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, AllyInviteSubCommands.ACCEPT)
            return
        }
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        val clanSender = ClanManager.getClanByName(args[0])
        if(clanSender == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        try{
            AllyInviteManager.acceptAllyInvite(receiver = clan, sender = clanSender)
        } catch (e: Exception) {
            e.printStackTrace()
            commandSender.sendMessage("Erro ${e.message}".color(ChatColor.RED).bold())
        }
        commandSender.sendMessage("Convite aceito".color(ChatColor.GREEN))
        Bukkit.getPlayer(PlayerManager.getPlayerById(clanSender.owner)!!.mineId)
            ?.sendMessage("O seu clã se aliou com o clã ${clan.name}.")
    }
    
    private fun reject(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, AllyInviteSubCommands.REJECT)
            return
        }
        
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null){
            CommandException.notClanLeader(commandSender)
            return
        }
        
        val clanSender = ClanManager.getClanByName(args[0])
        if(clanSender == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        
        AllyInviteManager.rejectAllyInvite(clan, clanSender)
        commandSender.sendMessage("Convite recusado".color(ChatColor.RED))
        Bukkit.getPlayer(PlayerManager.getPlayerById(clanSender.owner)!!.mineId)
            ?.sendMessage("O clã ${clan.name.bold()} rejeitou a sua aliança.".color(ChatColor.RED))
    }
    
    private fun send(commandSender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            CommandException.sendUsage(commandSender, AllyInviteSubCommands.SEND)
            return
        }
        
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if (clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        val clanReceiver = ClanManager.getClanByName(args[0])
        if (clanReceiver == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        
        if(clanReceiver.isAlliedWith(clan)) {
            commandSender.sendMessage("Você ja é aliado com o clã ${clanReceiver.name.bold()}")
            return
        }
        
        AllyInviteManager.addAllyInvite(clan, clanReceiver)
        Bukkit.getPlayer(PlayerManager.getPlayerById(clanReceiver.owner)!!.mineId)?.sendMessage("Convite de aliança do clã: ${clan.name.bold()} escreva ${"/ally invite accept ${clan.name}".bold()} para aceitar.")
        commandSender.sendMessage("Convite enviado.".color(ChatColor.GREEN))
    }
    
    private fun remove(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, AllyCommands.REMOVE)
            return
        }
        
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        val clanReceiver = ClanManager.getClanByName(args[0])
        if(clanReceiver == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        if(clan.allies.contains(clanReceiver._id)) {
            ClanManager.removeAlly(clan, clanReceiver)
            commandSender.sendMessage("Clãs removidos como aliados.".color(ChatColor.DARK_RED))
            Bukkit.getPlayer(PlayerManager.getPlayerById(clanReceiver.owner)!!.mineId)
                ?.sendMessage("O seu clã se desaliou do clã ${clan.name}.".color(ChatColor.DARK_RED))
        } else {
            commandSender.sendMessage("Esta clã não é um aliado da sua.".color(ChatColor.RED))
        }
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        return when (p3.size) {
            1 -> AllyCommands.entries.map { it.command }
            2 -> when (p3[0]?.lowercase()) {
//                AllyCommands.ACCEPT.command -> listAllyInvites(p0 as Player)
//                AllyCommands.REJECT.command -> listAllyInvites(p0 as Player)
//                AllyCommands.SEND.command -> listAllClans(p0 as Player)
                AllyCommands.INVITE.command -> AllyInviteSubCommands.entries.map { it.command }
//                AllyCommands.LIST.command -> AllyListSubCommands.entries.map { it.command }
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
        val invites = AllyInviteManager.getAllyInvitesByReceiver(receiverClan)
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