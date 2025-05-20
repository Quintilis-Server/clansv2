package org.quintilis.clansv2.commands

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.ally.AllyCommands
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

object CommandException {
    fun notPlayer(commandSender: CommandSender):Boolean {
        commandSender.sendMessage("You must be a player!".color(ChatColor.RED))
        return true
    }
    fun sendAllUsage(commandSender: CommandSender, commands: Array<String>): Boolean {
        var out = "";
        for(command in commands) {
            out += "${command.color(ChatColor.YELLOW).bold()}, "
        }
        commandSender.sendMessage(
            "Uso: $out"
        )
        return true
    }
    fun notClanLeader(commandSender: CommandSender): Boolean {
        commandSender.sendMessage("Você tem que ser o líder do clã para fazer isso!".color(ChatColor.RED))
        return true
    }
    
    fun notEnoughArgs(commandSender: CommandSender, args: Array<out String>, min: Int): Boolean {
        commandSender.sendMessage("Argumentos insuficientes. Necessário $min, fornecido ${args.size}".color(ChatColor.RED))
        return true
    }
    
    fun notFound(commandSender: CommandSender, type:String): Boolean{
        commandSender.sendMessage("O $type não foi encontrado.".color(ChatColor.RED))
        return true
    }
    
}