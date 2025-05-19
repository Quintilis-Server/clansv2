package org.quintilis.clansv2.managers

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.quintilis.clansv2.entities.ClanEntity

object EnemyManager {
    fun list(playerOwner: Player): List<ClanEntity> {
        val clanOwner = ClanManager.getClanByOwner(playerOwner)
        println(clanOwner)
        val enemies: List<ClanEntity> = clanOwner!!.enemies.mapNotNull {
            ClanManager.getClanById(it)
        }
        return enemies
    }
    
    fun add(receiver: ClanEntity, playerSender: Player) {
        val clanSender = ClanManager.getClanByOwner(playerSender)!!
        ClanManager.addEnemy(clanSender, receiver)
        
        Bukkit.getOnlinePlayers().forEach {
            it.sendTitle(ChatColor.RED.toString()+ "GUERRA!", "${clanSender.name} agora está em GUERRA com o clã ${receiver.name}", 10, 70, 20)
            it.sendMessage(ChatColor.DARK_RED.toString() + "Agora as mortes valem: ")
        }
    }
    
    fun remove(receiver: ClanEntity, sender: ClanEntity) {
        ClanManager.sendMessageToMembers(receiver, "${sender.name} fez as pazes")
    }
}