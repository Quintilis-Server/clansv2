package org.quintilis.clansv2.commands.clan

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class ClanCommand: CommandExecutor, TabExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            p0.sendMessage(ChatColor.RED.toString() + "You must be a player!")
            return true
        }
        if(p3.isEmpty()) {
            p0.sendMessage("Uso: /clan <create|delete|list>")
            return true
        }
        return when(p3[0] as ClanCommands){
            ClanCommands.CREATE -> createClan(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.DELETE -> TODO()
            ClanCommands.LIST -> TODO()
        }
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        return ClanCommands.entries.map { it.command }
    }
    
    fun createClan(commandSender: CommandSender, args: Array<out String>): Boolean {
        val name: String = args[0]
        if(name.isEmpty()) {
            commandSender.sendMessage("Nome do clã esta vazio, uso /")
            
        }
        return true
    }
    
}