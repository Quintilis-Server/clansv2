package org.quintilis.clansv2.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.ConfigManager
import org.quintilis.clansv2.managers.PlayerManager

class DeathEventListener(
    private val configManager: ConfigManager
): Listener {
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val victim = event.entity;
        val killer = victim.killer ?: return
        
        var victimEntity = PlayerManager.getPlayerEntityByPlayer(victim)
        var killerEntity = PlayerManager.getPlayerEntityByPlayer(killer)
        if (victimEntity == null||killerEntity == null) {
            victimEntity = PlayerManager.saveIfNotExists(victim)!!
            killerEntity = PlayerManager.saveIfNotExists(killer)!!
        }
        
        val victimClan = ClanManager.getClanByMember(victim)
        val killerClan = ClanManager.getClanByMember(killer)
        
        //somente se algum dos envolvidos não tem clã
        //depois desse if os clãs não são null, mesmo que a IDE fale o contrario
        if(victimClan == null||killerClan == null) {
            stealPoints(killerEntity, victimEntity, configManager.noEnemyKillPoints)
            if(victimClan != null){
                ClanManager.updateClanPoints(victimClan)
            }else if(killerClan != null){
                ClanManager.updateClanPoints(killerClan)
            }
            return
        }
        
        //somente se os clãs são aliados
        if(ClanManager.isAlly(victimClan,killerClan)){
            stealPoints(killerEntity,victimEntity,configManager.allyKillPoints)
            updateClanPoints(victimClan,killerClan)
            return;
        }
        
        //somente se o clã for o mesmo
        if(victimClan == killerClan) {
            updateClanPoints(victimClan,killerClan)
            return;
        }
        
        //somente se os clãs nao sao inimigos
        stealPoints(killerEntity,victimEntity, configManager.noEnemyKillPoints)
        updateClanPoints(victimClan,killerClan)
    }
    
    private fun stealPoints(killerEntity: PlayerEntity, victimEntity: PlayerEntity, adicionalPoints: Int = 0){
        killerEntity.addPoints(victimEntity.getPoints() + adicionalPoints)
        victimEntity.removePoints(victimEntity.getPoints())
    }
    
    private fun updateClanPoints(victimClan: ClanEntity, killerClan: ClanEntity){
        ClanManager.updateClanPoints(victimClan)
        ClanManager.updateClanPoints(killerClan)
    }
}