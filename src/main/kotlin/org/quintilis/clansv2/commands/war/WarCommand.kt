package org.quintilis.clansv2.commands.war

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.CommandException
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.EnemyManager

class WarCommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player){
            return CommandException.notPlayer(p0)
        }
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, WarCommandsEnum.entries.map { it.usage }.toTypedArray())
        }
        if(!ClanManager.isOwner(p0)){
            return CommandException.notClanLeader(p0)
        }
        
        when(p3[0]) {
            WarCommandsEnum.DECLARE.command -> declare(p0,p3.sliceArray(1 until p3.size))
        }
        return true;
    }
    
    private fun declare(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendAllUsage(commandSender, arrayOf(WarCommandsEnum.DECLARE.usage))
            return
        }
        
        val clan = ClanManager.getClanByName(args[0])
        if(clan == null) {
            CommandException.notFound(commandSender,"clã")
            return
        }
        val clanSender = ClanManager.getClanByOwner(commandSender as Player)
        if(clanSender == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        if(clan.allies.contains(clanSender._id) || clanSender.enemies.contains(clan._id)) {
            if (args.size < 2 || !args[1].equals("sim", ignoreCase = true)) {
                commandSender.sendMessage(
                    "Os clãs são aliados! " +
                            "Se tiver certeza, repita o comando para ${ChatColor.BOLD}realizar o ato hediondo${ChatColor.RESET}:\n" +
                            "${ChatColor.YELLOW}${ChatColor.BOLD}/clan enemy remove ${clan.name} sim"
                )
                return
            }
//            commandSender.sendMessage("Os clãs são aliados, escreva \"sim\" para ${ChatColor.BOLD}realizar o ato hediondo")
        }
        
        EnemyManager.add(clan,commandSender)
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): List<String?>? {
        return when(p3.size) {
            1 -> WarCommandsEnum.entries.map { it.command }
            2 -> ClanManager.getAllClans().filterNot { it._id == ClanManager.getClanByOwner(p0 as Player)!!._id }.map { it.name }
            else -> listOf()
        }
        
    }
}