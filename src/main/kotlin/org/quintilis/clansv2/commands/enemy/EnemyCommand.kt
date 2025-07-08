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
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color
import org.quintilis.clansv2.string.italic

class EnemyCommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, EnemyCommands.entries.toTypedArray())
        }
        
        if(!ClanManager.isOwner(p0)){
            return CommandException.notClanLeader(p0)
        }
        
        when(p3[0]) {
            EnemyCommands.LIST.command -> {
                p0.sendMessage(EnemyManager.list(p0).joinToString(", ") { it.name })
            }
            EnemyCommands.REMOVE.command -> remove(p0,p3.sliceArray(1 until p3.size))
            EnemyCommands.DECLARE.command -> declare(p0,p3.sliceArray(1 until p3.size))
            else -> {
                CommandException.sendAllUsage(p0, EnemyCommands.entries.toTypedArray())
            }
        }
        
        return true;
    }
    
    private fun remove(commandSender: CommandSender, args: Array<out String>) {
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, EnemyCommands.REMOVE)
            return
        }
        
        val clan = ClanManager.getClanByName(args[0])!!
        val clanSender = ClanManager.getClanByOwner(commandSender as Player)!!
        
        if(!clan.enemies.contains(clanSender._id) || !clanSender.enemies.contains(clan._id)) {
            commandSender.sendMessage("Os clãs não são ${ChatColor.BOLD}${ChatColor.YELLOW} inimigos")
            return
        }
        
        ClanManager.removeEnemy(clan,clanSender)
        commandSender.sendMessage("Removido ${clan.name.bold()} como inimigo do clã ${clanSender.name.bold()}")
        ClanManager.sendMessageToMembers(clan,"${clan.name.bold()} fez as pazes com o clã ${clanSender.name.bold()}")
        ClanManager.sendMessageToMembers(clanSender,"${clanSender.name.bold()} fez as pazes com o clã ${clan.name.bold()}")
    }
    
    private fun declare(commandSender: CommandSender, args: Array<String>) {
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, EnemyCommands.DECLARE)
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
                            "Se tiver certeza, repita o comando para ${"realizar o ato hediondo".bold().italic().color(ChatColor.DARK_RED)}:\n" +
                            "/ally remove ${clan.name}".color(ChatColor.YELLOW)
                )
                return
            }
        }
        
        EnemyManager.add(clan,commandSender)
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