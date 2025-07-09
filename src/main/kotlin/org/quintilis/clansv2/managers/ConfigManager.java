package org.quintilis.clansv2.managers;

import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
    }

    public int getNoEnemyKillPoints(){
        return this.config.getInt("killPoints.noEnemy");
    }

    public int getEnemyKillPoints(){
        return this.config.getInt("killPoints.enemy");
    }

    public int getAllyKillPoints(){
        return this.config.getInt("killPoints.ally");
    }

    public int getPlayerInviteExpirationHours(){
        return this.config.getInt("invite.player.expiration");
    }

    public int getAllyInviteExpirationHours(){
        return this.config.getInt("invite.ally.expiration");
    }

    public String getDatabaseURI(){
        return this.config.getString("database.mongodb.uri");
    }

    @Override
    public String toString(){
        return this.config.getCurrentPath();
    }
}
