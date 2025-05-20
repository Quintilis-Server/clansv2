package org.quintilis.clansv2.commands.enemy

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.CommandException
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.EnemyManager

class EnemyCommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, EnemyCommands.entries.map { it.usage }.toTypedArray())
        }
        
        if(!ClanManager.isOwner(p0)){
            return CommandException.notClanLeader(p0)
        }
        
        when(p3[0]) {
            EnemyCommands.LIST.command -> {
                p0.sendMessage(EnemyManager.list(p0).joinToString(", ") { it.name })
            }
            EnemyCommands.REMOVE.command -> remove(p0,p3.sliceArray(1 until p3.size))
        }
        
        return true;
    }
    
    private fun remove(commandSender: CommandSender, args: Array<out String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(EnemyCommands.REMOVE.usage))
            return
        }
        
        val clan = ClanManager.getClanByName(args[0])!!
        val clanSender = ClanManager.getClanByOwner(commandSender as Player)!!
        
        if(!clan.enemies.contains(clanSender._id) || !clanSender.enemies.contains(clan._id)) {
            commandSender.sendMessage("Os clãs não são ${ChatColor.BOLD}${ChatColor.YELLOW} inimigos")
            return
        }
        
        EnemyManager.remove(clan, clanSender)
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        if(p3.size == 1) {
            return EnemyCommands.entries.map { it.command }
        }else if(p3.size == 2) {
            return when(p3[0]) {
                EnemyCommands.LIST.command -> listOf()
                EnemyCommands.REMOVE.command -> EnemyManager.list(p0 as Player).map { it.name }
                else -> return listOf()
            }
        }
        return null
    }
    
}