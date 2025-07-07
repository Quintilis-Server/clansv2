package org.quintilis.clansv2.commands.clan

import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.quintilis.clansv2.commands.CommandException
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.InviteManager
import org.quintilis.clansv2.managers.PlayerManager
import org.quintilis.clansv2.string.bold
import org.quintilis.clansv2.string.color

class ClanCommand: CommandExecutor, TabExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        if(p0 !is Player) {
            return CommandException.notPlayer(p0)
        }
        if(p3.isEmpty()) {
            return CommandException.sendAllUsage(p0,ClanCommands.entries.toTypedArray())
        }
        when(p3[0]){
            ClanCommands.CREATE.command -> create(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.DELETE.command -> delete(p0)
            ClanCommands.LIST.command -> list(p0)
            ClanCommands.SET.command -> set(p0, p3.sliceArray(1 until p3.size))
            ClanCommands.QUIT.command -> quit(p0)
            ClanCommands.MEMBER.command -> {
                val newArray = p3.sliceArray(1 until p3.size)
                if(newArray.isEmpty()) {
                    return CommandException.sendAllUsage(p0, ClanMemberSubCommands.entries.toTypedArray())
                }
                when(p3[1]){
                    ClanMemberSubCommands.INVITE.command -> sendInvite(p0, p3.sliceArray(2 until p3.size))
                    ClanMemberSubCommands.KICK.command -> kick(p0, p3.sliceArray(2 until p3.size))
                    ClanMemberSubCommands.LIST.command -> listMembers(p0, p3.sliceArray(2 until p3.size))
                    else -> return CommandException.sendAllUsage(p0, ClanMemberSubCommands.entries.toTypedArray())
                }
            }
            else -> return CommandException.sendAllUsage(p0, ClanCommands.entries.toTypedArray())
        }
        return true
    }
    
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<String?>): List<String?>? {
        val clan = ClanManager.getClanByOwner(p0 as Player)
//        if(clan == null){
//
//        }
        return when(p3.size){
            1->{
                ClanCommands.entries.map { it.command }
            }
            2->{
                when(p3[0]){
                    ClanCommands.SET.command->{
                        if(clan == null) return emptyList()
                        ClanSetSubCommands.entries.map { it.command }
                    }
                    ClanCommands.MEMBER.command->{
                        if(clan != null){
                            ClanMemberSubCommands.entries.map { it.command }
                        }else{
                            listOf(ClanMemberSubCommands.LIST.command)
                        }
                    }
                    else -> listOf()
                }
            }
            3->{
                if(clan!=null){
                    return when(p3[1]){
                        ClanMemberSubCommands.INVITE.command -> Bukkit.getOnlinePlayers().filterNot { it -> clan.members.contains(PlayerManager.getPlayerByMineId(it.uniqueId)?._id) }.map { it.name }
                        ClanMemberSubCommands.KICK.command -> clan.members.filterNot { it == clan.owner }.map { PlayerManager.getPlayerById(it)?.name }
                        ClanMemberSubCommands.LIST.command -> ClanManager.getAllClans().map { it.name }
                        else -> emptyList()
                    }
                }else{
                    return ClanManager.getAllClans().map { it.name }
                }
            }
            else -> emptyList()
        }
    }
    
    
    private fun create(commandSender: CommandSender, args: Array<String>) {
        
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, ClanCommands.CREATE)
            return
        }
        
        val name = args.getOrNull(0)
        
        if(name==null || name.isEmpty()) {
            commandSender.sendMessage("Nome do clã esta vazio, uso /clan create <nome do clã> <tag>")
            return
        }
        
        val tag: String? = args.getOrNull(1)
        
        val player = commandSender as Player
        val playerEntity = PlayerManager.getPlayerByMineId(player.uniqueId)
        
        println("$playerEntity")
        
        if (playerEntity == null) {
            commandSender.sendMessage("Erro: jogador não encontrado no banco de dados.")
            return
        }
        
        if(playerEntity.clanId != null){
            CommandException.alreadyInClan(commandSender);
            return
        }
        
        val clan = ClanEntity(name = name, tag = tag, owner = PlayerManager.getPlayerByMineId((commandSender).uniqueId)!!._id, _id = ObjectId())
        ClanManager.create(clan,commandSender)
        
        commandSender.sendMessage("Clã ${clan.name} criado com sucesso!")
    }
    
    private fun delete(commandSender: CommandSender){
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan== null) {
            commandSender.sendMessage("Você não é dono de nenhum clã!")
            return
        }
        ClanManager.delete(clan)
        commandSender.sendMessage("Clã deletado com sucesso!")
    }
    
    private fun list(commandSender: CommandSender){
        var out = ""
        ClanManager.listClans().forEach {
            println(it)
            out += ("Nome:".color(ChatColor.YELLOW).bold() + it.name + ", " +
                    "Tag: ".color(ChatColor.YELLOW).bold() + (it.tag ?: "") + ", " +
                    "Dono: ".color(ChatColor.YELLOW).bold() + PlayerManager.getPlayerById(it.owner)!!.name + ", " +
                    "Pontos: ".color(ChatColor.YELLOW).bold() + it.points +
                    "\n")
        }
        commandSender.sendMessage(out)
    }
    
    private fun set(commandSender: CommandSender, args: Array<String>){
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        val setValue = args.getOrNull(1)
        if(setValue == null) {
            CommandException.sendAllUsage(commandSender, ClanSetSubCommands.entries.toTypedArray())
            return
        }
        
        when(args.getOrNull(0)){
            ClanSetSubCommands.NAME.command -> ClanManager.setName(setValue, clan)
            ClanSetSubCommands.TAG.command -> ClanManager.setTag(setValue, clan)
            else -> {
                CommandException.sendAllUsage(commandSender, ClanSetSubCommands.entries.toTypedArray())
                return
            }
        }
        commandSender.sendMessage("Valor ${args.getOrNull(0)} alterado para: $setValue")
    }
    
    //member commands
    
    private fun kick(commandSender: CommandSender, args: Array<String>){
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, ClanMemberSubCommands.KICK)
            return
        }
        
        val playerReceiver = PlayerManager.getPlayerByName(args[0])
        if(playerReceiver == null){
            CommandException.notFound(commandSender, "jogador")
            return
        }
        if(playerReceiver._id == clan.owner) {
            commandSender.sendMessage("Você não pode remover o dono do clã.".color(ChatColor.RED).bold())
            return
        }
        
        ClanManager.removeMember(clan, playerReceiver)
        ClanManager.sendMessageToMembers(clan,"${"Usuário".color(ChatColor.RED)} ${playerReceiver.name.bold()} ${"foi removido do clã".color(
            ChatColor.RED)} ${clan.name.bold().color(ChatColor.RED)}")
    }
    
    private fun sendInvite(commandSender: CommandSender, args: Array<String>){
        val clan = ClanManager.getClanByOwner(commandSender as Player)
        if(clan == null) {
            CommandException.notClanLeader(commandSender)
            return
        }
        
        if(args.isEmpty()) {
            CommandException.sendUsage(commandSender, ClanMemberSubCommands.INVITE)
            return
        }
        
        val playerSender: PlayerEntity = PlayerManager.getPlayerByMineId(commandSender.uniqueId)!!
        val playerReceiver = PlayerManager.getPlayerByName(args[0])
        if(playerReceiver == null){
            CommandException.notFound(commandSender, "jogador")
            return
        }
        try{
            InviteManager.addPlayerInvite(playerSender, playerReceiver, clan)
        }catch (e: Exception){
            commandSender.sendMessage("Erro ao mandar convite, talvez já tenha um convite para essa pessoa.".color(ChatColor.RED).bold())
            println(e.message)
        }
        commandSender.sendMessage("Convite enviado ${playerReceiver.name.bold().color(ChatColor.YELLOW)} para o clã ${clan.name.bold().color(ChatColor.YELLOW)}")
    }
    
    private fun listMembers(commandSender: CommandSender, args: Array<String>){
        var clan: ClanEntity? = null;
        if(!args.isEmpty()){
            clan = ClanManager.getClanByName(args[0])
        }else{
            clan = ClanManager.getClanByMember(commandSender as Player)
        }
        if(clan == null){
            CommandException.notInAClan(commandSender)
            return
        }
        commandSender.sendMessage("Membros do clã ${clan.name.bold()}".color(ChatColor.YELLOW))
        clan.members.forEach {
            val player = PlayerManager.getPlayerById(it)
            commandSender.sendMessage("${
                if (clan.owner == it) {
                    "[${"Dono".color(ChatColor.AQUA).bold()}]"
                }
                else{;
                    
                    "[${"Membro".color(ChatColor.GREEN).bold()}]"
                }
            } ${player?.name!!.bold()}"
            )
        }
    }
    
    fun quit(commandSender: CommandSender){
        if(commandSender !is Player){
            CommandException.notPlayer(commandSender)
            return
        }
        if(!ClanManager.isInClan(player = commandSender)){
            CommandException.notInAClan(commandSender)
            return
        }
        if(ClanManager.isOwner(commandSender)){
            commandSender.sendMessage("Você é o dono do clã, use o ${ClanCommands.DELETE.usage.color(ChatColor.RED).bold()} para deletar o clã")
            return
        }
        val clan = ClanManager.getClanByMember(commandSender)!!
        ClanManager.removeMember(commandSender)
        commandSender.sendMessage("Você saiu do clã ${clan.name.bold()}".color(ChatColor.RED).bold())
    }
    
}