package org.quintilis.clansv2.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.quintilis.clansv2.entities.ClanEntity
import org.quintilis.clansv2.entities.DeathEntity
import org.quintilis.clansv2.entities.PlayerEntity
import org.quintilis.clansv2.managers.ClanManager
import org.quintilis.clansv2.managers.ConfigManager
import org.quintilis.clansv2.managers.DeathManager
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
        
        val deathEntity = DeathEntity(
            victim = victimEntity._id,
            killer = killerEntity._id
        )
        
        val victimClan = ClanManager.getClanByMember(victim)
        val killerClan = ClanManager.getClanByMember(killer)
        
        //somente se algum dos envolvidos não tem clã
        //depois desse if os clãs não são null, mesmo que a IDE fale o contrario
        if(victimClan == null||killerClan == null) {
            stealPoints(
                killerEntity,
                victimEntity,
                adicionalPoints = configManager.noEnemyKillPoints)
            if(victimClan != null){
                ClanManager.updateClanPoints(victimClan)
            }else if(killerClan != null){
                ClanManager.updateClanPoints(killerClan)
            }
            DeathManager.save(deathEntity)
            return
        }
        
        //somente se os clãs são aliados
        if(ClanManager.isAlly(victimClan,killerClan)){
            stealPoints(
                killerEntity,
                victimEntity,
                adicionalPoints = configManager.allyKillPoints)
            deathEntity.isAlly = true;
            DeathManager.save(deathEntity)
            updateClanPoints(victimClan,killerClan)
            return;
        }
        
        //somente se o clã for o mesmo
        if(victimClan == killerClan) {
            deathEntity.sameClan = true;
            DeathManager.save(deathEntity)
            updateClanPoints(victimClan,killerClan)
            return;
        }
        
        //somente se os clãs são inimigos
        if(ClanManager.isEnemy(victimClan,killerClan)){
            stealPoints(
                killerEntity,
                victimEntity,
                adicionalPoints = configManager.enemyKillPoints
            )
            deathEntity.isEnemy = true;
            DeathManager.save(deathEntity)
            updateClanPoints(victimClan,killerClan)
            return
        }
        
        //somente se os clãs nao sao inimigos
        stealPoints(
            killerEntity,
            victimEntity,
            adicionalPoints = configManager.noEnemyKillPoints
        )
        DeathManager.save(deathEntity)
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