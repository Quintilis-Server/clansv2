package org.quintilis.clansv2.commands

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.ally.AllyCommands

object CommandException {
    fun notPlayer(commandSender: CommandSender):Boolean {
        commandSender.sendMessage(ChatColor.RED.toString() + "You must be a player!")
        return true
    }
    fun sendAllUsage(commandSender: CommandSender, commands: Array<out String>): Boolean {
        var out: String = "";
        for(command in commands) {
            out += "/$command "
        }
        commandSender.sendMessage(
            "Usage: " + ChatColor.YELLOW + out
        )
        return true
    }
    fun notClanLeader(commandSender: CommandSender): Boolean {
        commandSender.sendMessage(ChatColor.RED.toString() + "Você tem que ser o líder do clã para fazer isso!")
        return true
    }
    
    fun notEnoughArgs(commandSender: CommandSender, args: Array<out String>, min: Int): Boolean {
        commandSender.sendMessage(ChatColor.RED.toString() + "Argumentos insuficientes. Necessário $min, fornecido ${args.size}")
        return true
    }
    
}