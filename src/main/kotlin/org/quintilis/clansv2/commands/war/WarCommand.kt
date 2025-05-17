package org.quintilis.clansv2.commands.war

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
            return CommandException.sendAllUsage(p0, WarCommandEnum.entries.map { it.usage }.toTypedArray())
        }
        if(!ClanManager.isOwner(p0)){
            return CommandException.notClanLeader(p0)
        }
        
        when(p3[0]) {
            WarCommandEnum.DECLARE.command -> EnemyManager.add(ClanManager.getClanByName(p3[1])!!,p0)
        }
        return true;
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): List<String?>? {
        TODO("Not yet implemented")
    }
}