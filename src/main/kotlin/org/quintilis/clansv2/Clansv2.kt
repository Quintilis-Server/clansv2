package org.quintilis.clansv2

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.quintilis.clansv2.commands.ally.AllyCommand
import org.quintilis.clansv2.commands.clan.ClanCommand
import org.quintilis.clansv2.commands.clan.ClanCommands
import org.quintilis.clansv2.commands.enemy.EnemyCommand
import org.quintilis.clansv2.commands.invite.InviteCommand
import org.quintilis.clansv2.commands.war.WarCommand
import org.quintilis.clansv2.events.PlayerEventListener
import org.quintilis.clansv2.managers.InviteManager
import org.quintilis.clansv2.managers.MongoManager
import org.quintilis.clansv2.string.color

class Clansv2 : JavaPlugin() {
    
    private val logger = Bukkit.getLogger()
    
    override fun onEnable() {
        logger.info("Clansv2 is enabled!")
        if(!this.configFileExists()) {
            this.saveDefaultConfig()
        }
        logger.info(
            """
            Configuration loaded:
            ${config.getString("database.mongodb.uri")}
            """.trimIndent()
        )
        if (!MongoManager.connect(config.getString("database.mongodb.uri")!!)) {
            logger.severe("Falha ao conectar ao MongoDB. Desligando o servidor.".color(ChatColor.RED))
            Bukkit.getScheduler().runTask(this, Runnable {
                Bukkit.shutdown()
            })
            return
        }
        InviteManager.setConfig(
            this.config.getInt("invite.ally.expiration"),
            this.config.getInt("invite.player.expiration")
        )
        
        Bukkit.getPluginManager().registerEvents(PlayerEventListener(), this)
        
        
        //comandos
        this.getCommand("clan")?.setExecutor(ClanCommand())
        this.getCommand("ally")?.setExecutor(AllyCommand())
        this.getCommand("enemy")?.setExecutor(EnemyCommand())
        this.getCommand("war")?.setExecutor(WarCommand())
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
