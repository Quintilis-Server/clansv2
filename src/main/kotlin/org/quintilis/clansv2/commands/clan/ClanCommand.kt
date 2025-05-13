package org.quintilis.clansv2.commands.clan

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates.set
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.set
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity

class ClanCommand(
    private val clanColllection: MongoCollection<ClanEntity>,
    private val playerCollection: MongoCollection<PlayerEntity>
): CommandExecutor, TabExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            p0.sendMessage(ChatColor.RED.toString() + "You must be a player!")
            return true
        }
        if(p3.isEmpty()) {
            p0.sendMessage("Uso: /clan <create|delete|list>")
            return true
        }
        when(p3[0] as ClanCommands){
            ClanCommands.CREATE -> create(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.DELETE -> delete(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.LIST -> TODO()
        }
        return true
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String?>): List<String?>? {
        if(p3.size == 1) {
            return ClanCommands.entries.map { it.command }
        }else if (p3.size == 2) {
            return listOf()
        }
        return null
    }
    
    
    fun create(commandSender: CommandSender, args: Array<out String>) {
        val name: String = args[0]
        if(name.isEmpty()) {
            commandSender.sendMessage("Nome do clã esta vazio, uso /clan create <nome do clã> <tag>")
            return
        }
        val tag: String? = args[1]
        val clan = ClanEntity(name = name, tag = tag, owner = (commandSender as Player).uniqueId)
        clanColllection.insertOne(clan)
        commandSender.sendMessage("Clã ${clan.name} criado com sucesso!")
    }
    
    fun delete(commandSender: CommandSender, args: Array<out String>){
        val clan: FindIterable<ClanEntity?> = clanColllection.find(eq("owner", (commandSender as Player).uniqueId))
        if(clan.first() == null) {
            commandSender.sendMessage("Você não é dono de nenhum clã!")
            return
        }
        clan.first()!!.members.forEach {
            playerCollection.updateOne(
                eq("_id", it),
                set("clan", null)
            )
            Bukkit.getPlayer(it)?.sendMessage("Você foi removido do clã ${clan.first()?.name} pois você se desligou do servidor.")
        }
        clanColllection.deleteOne(
            eq("owner", commandSender.uniqueId)
        )
        commandSender.sendMessage("Clã deletado com sucesso!")
    }
    
    fun list(commandSender: CommandSender){
        var out: String = "";
        clanColllection.find().forEach {
            out += it.toString() + "\n"
        }
        commandSender.sendMessage(out)
    }
    
}