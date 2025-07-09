package org.quintilis.clansv2

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.quintilis.clansv2.commands.ally.AllyCommand
import org.quintilis.clansv2.commands.clan.ClanCommand
import org.quintilis.clansv2.commands.enemy.EnemyCommand
import org.quintilis.clansv2.commands.invite.InviteCommand
import org.quintilis.clansv2.events.DeathEventListener
import org.quintilis.clansv2.events.PlayerEventListener
import org.quintilis.clansv2.luckperms.LuckPermsInitializer
import org.quintilis.clansv2.managers.AllyInviteManager
import org.quintilis.clansv2.managers.ConfigManager
import org.quintilis.clansv2.managers.InviteManager
import org.quintilis.clansv2.managers.MongoManager
import org.quintilis.clansv2.string.color


class Clansv2 : JavaPlugin() {
    
    private val logger = Bukkit.getLogger()
    private val configManager = ConfigManager(this.config)
    
    
    override fun onEnable() {
        logger.info("Clansv2 is enabled!")
        if(!this.configFileExists()) {
            this.saveDefaultConfig()
        }
        logger.info(
            """
            Configuration loaded:
            ${configManager.databaseURI}
            """.trimIndent()
        )
        
        val lpApi = try {
            LuckPermsProvider.get()
        } catch (ex: IllegalStateException) {
            logger.severe { ex.message }
            logger.warning("LuckPerms não disponível neste momento.")
            null
        }
        
        if (!MongoManager.connect(configManager.databaseURI)) {
            logger.severe("Falha ao conectar ao MongoDB. Desligando o servidor.".color(ChatColor.RED))
            Bukkit.getScheduler().runTask(this, Runnable {
                Bukkit.shutdown()
            })
            return
        }
        lpApi?.let {
            logger.info("LuckPerms encontrado via LuckPermsProvider → iniciando integração.")
            LuckPermsInitializer(this, it).initialize()
        }
        InviteManager.setConfig(
            configManager.playerInviteExpirationHours
        )
        AllyInviteManager.setConfig(
            configManager.allyInviteExpirationHours,
        )
        
        
        Bukkit.getPluginManager().registerEvents(PlayerEventListener(), this)
        Bukkit.getPluginManager().registerEvents(DeathEventListener(configManager), this)
        
        //comandos
        this.getCommand("clan")?.setExecutor(ClanCommand())
        this.getCommand("ally")?.setExecutor(AllyCommand())
        this.getCommand("enemy")?.setExecutor(EnemyCommand())
//        this.getCommand("war")?.setExecutor(WarCommand())
        this.getCommand("invite")?.setExecutor(InviteCommand())
    }
    
    override fun onDisable() {
        // Plugin shutdown logic
        MongoManager.close()
    }
    
    private fun configFileExists(): Boolean {
        val dataFolder = dataFolder
        val configFile = dataFolder.resolve("config.yml")
        return configFile.exists() && configFile.isFile
    }
}
