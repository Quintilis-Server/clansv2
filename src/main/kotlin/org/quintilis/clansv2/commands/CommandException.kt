package org.quintilis.clansv2.commands

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

object CommandException {
    fun notPlayer(commandSender: CommandSender):Boolean {
        commandSender.sendMessage("You must be a player!".color(ChatColor.RED))
        return true
    }
    fun <T> sendAllUsage(commandSender: CommandSender, commands: Array<T>): Boolean where T: Enum<T>, T: CommandInterface{
        val out = commands.joinToString(" | ".color(ChatColor.RESET)) { it.usage.color(ChatColor.YELLOW).bold() }
        commandSender.sendMessage("Uso: $out")
        return true
    }
    fun <T> sendUsage(commandSender: CommandSender, commands: T): Boolean where T: Enum<T>, T: CommandInterface {
        commandSender.sendMessage("Uso: ${commands.usage.color(ChatColor.YELLOW).bold()}")
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
    
    fun notInAClan(commandSender: CommandSender): Boolean {
        commandSender.sendMessage("Você não esta em um clã.".color(ChatColor.RED).bold())
        return true
    }
    
    fun alreadyInClan(commandSender: CommandSender): Boolean {
        commandSender.sendMessage("Você ja esta em um clã.".color(ChatColor.RED))
        return true
    }
}