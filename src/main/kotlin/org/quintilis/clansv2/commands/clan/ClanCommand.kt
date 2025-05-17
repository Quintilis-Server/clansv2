package org.quintilis.clansv2.commands.clan

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.set
import org.bson.types.ObjectId
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
import org.quintilis.clansv2.managers.PlayerManager

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
            ClanCommands.DELETE.command -> delete(p0)
            ClanCommands.LIST.command -> list(p0)
        }
        return true
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<String?>): List<String?>? {
        if(p3.size == 1) {
            return ClanCommands.entries.map { it.command }
        }else {
            return listOf()
        }
//        return null
    }
    
    
    fun create(commandSender: CommandSender, args: Array<String>) {
        val name: String = args.get(0)
        if(name.isEmpty()) {
            commandSender.sendMessage("Nome do clã esta vazio, uso /clan create <nome do clã> <tag>")
            return
        }
        val tag: String? = args.getOrNull(1)
        println("$name $tag")
        val player = commandSender as Player
        val playerEntity = PlayerManager.getPlayerByMineId(player.uniqueId)
        println("$playerEntity")
        if (playerEntity == null) {
            commandSender.sendMessage("Erro: jogador não encontrado no banco de dados.")
            return
        }
        val clan = ClanEntity(name = name, tag = tag, owner = PlayerManager.getPlayerByMineId((commandSender).uniqueId)?._id, _id = ObjectId())
        ClanManager.create(clan,commandSender)
        commandSender.sendMessage("Clã ${clan.name} criado com sucesso!")
    }
    
    fun delete(commandSender: CommandSender){
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
            println(it)
            out += "Nome:${it.name}, Tag: ${it.tag?:""}, Dono: ${PlayerManager.getPlayerById(it.owner!!)!!.name} \n"
        }
        commandSender.sendMessage(out)
    }
    
}