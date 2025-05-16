package org.quintilis.clansv2.commands.clan

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.set
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.CommandException
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.managers.ClanManager

class ClanCommand(
    private val clanColllection: MongoCollection<ClanEntity>,
    private val playerCollection: MongoCollection<PlayerEntity>
): CommandExecutor, TabExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0, ClanCommands.entries.map { it.usage }.toTypedArray())
        }
        when(p3[0]){
            ClanCommands.CREATE.command -> create(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.DELETE.command -> delete(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.LIST.command -> list(p0)
        }
        return true
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        if(p3.size == 1) {
            return ClanCommands.entries.map { it.command }
        }else {
            return listOf()
        }
//        return null
    }
    
    
    fun create(commandSender: CommandSender, args: Array<out String>) {
        val name: String = args[0]
        if(name.isEmpty()) {
            commandSender.sendMessage("Nome do clã esta vazio, uso /clan create <nome do clã> <tag>")
            return
        }
        val tag: String? = args[1]
        val clan = ClanEntity(name = name, tag = tag, owner = (commandSender as Player).uniqueId)
        ClanManager.create(clan)
        commandSender.sendMessage("Clã ${clan.name} criado com sucesso!")
    }
    
    fun delete(commandSender: CommandSender, args: Array<out String>){
        val clan: FindIterable<ClanEntity?> = clanColllection.find(eq("owner", (commandSender as Player).uniqueId))
        if(clan.first() == null) {
            commandSender.sendMessage("Você não é dono de nenhum clã!")
            return
        }
        ClanManager.delete(clan.first()!!)
        commandSender.sendMessage("Clã deletado com sucesso!")
    }
    
    fun list(commandSender: CommandSender){
        var out = "";
        ClanManager.listClans().forEach {
            out += it.toString() + "\n"
        }
        commandSender.sendMessage(out)
    }
    
}