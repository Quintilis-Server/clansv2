package org.quintilis.clansv2.commands.invite

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
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.italic

class InviteCommand: CommandExecutor, TabCompleter {
    
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player){
            return CommandException.notPlayer(p0)
        }
        
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, InviteCommands.entries.map { it.usage }.toTypedArray())
        }
        
        when(p3[0]) {
            InviteCommands.ACCEPT.command -> accept(p0, p3.sliceArray(1 until p3.size))
            InviteCommands.REJECT.command -> reject(p0, p3.sliceArray(1 until p3.size))
            InviteCommands.LIST.command -> list(p0)
        }
        return true
    }
    
    private fun accept(commandSender: Player, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(InviteCommands.ACCEPT.usage))
            return
        }
        
        val clan = ClanManager.getClanByName(args[0])
        if(clan == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        
        InviteManager.acceptInvite(commandSender, clan)
        commandSender.sendMessage("Convite aceito".italic())
    }
    
    private fun reject(commandSender: Player, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(InviteCommands.REJECT.usage))
            return
        }
        
        val clan = ClanManager.getClanByName(args[0])
        if(clan == null) {
            CommandException.notFound(commandSender, "clã")
            return
        }
        
        InviteManager.rejectInvite(commandSender, clan)
        commandSender.sendMessage("Convite recusado".italic())
    }
    
    private fun list(commandSender: CommandSender){
        val invites = InviteManager.getPlayerInvitesByReceiver(PlayerManager.getPlayerByMineId((commandSender as Player).uniqueId)!!)
        commandSender.sendMessage(invites.joinToString { "$it, \n" }.ifEmpty { "Você não tem convites pendentes."})
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String>? {
        return when(p3.size) {
            1 -> InviteCommands.entries.map { it.command }
            2 -> when(p3[0]?.lowercase()) {
                InviteCommands.ACCEPT.command -> listInvites(p0).map { it.name }
                InviteCommands.REJECT.command -> listOf()
                else -> listOf()
            }
            else -> listOf()
        }
    }
    
    private fun listInvites(commandSender: CommandSender): List<ClanEntity>{
        return InviteManager.getPlayerInvitesByReceiver(PlayerManager.getPlayerByMineId((commandSender as Player).uniqueId)!!)
            .map {
                ClanManager.getClanById(it.clan!!)!!
            }
    }
    
}