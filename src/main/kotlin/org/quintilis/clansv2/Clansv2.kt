package org.quintilis.clansv2

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.quintilis.clansv2.managers.MongoManager

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
        MongoManager.connect(config.getString("database.mongodb.uri")!!)
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
