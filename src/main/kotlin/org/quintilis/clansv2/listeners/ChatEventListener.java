package org.quintilis.clansv2.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.quintilis.clansv2.entities.ClanEntity;
import org.quintilis.clansv2.entities.PlayerEntity;
import org.quintilis.clansv2.enums.UserRoles;
import org.quintilis.clansv2.managers.ClanManager;
import org.quintilis.clansv2.managers.PlayerManager;

public class ChatEventListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ClanEntity clan = ClanManager.INSTANCE.getClanByMember(player);
        event.setFormat(buildChatFormat(player,clan, event.getMessage()));
    }
    private String buildChatFormat(Player player, ClanEntity clan, String message) {
        String format = "";
        PlayerEntity playerEntity = PlayerManager.INSTANCE.getPlayerEntityByPlayer(player);
        if(playerEntity != null && playerEntity.getRole() != null) {
            UserRoles role = playerEntity.getRole();
            format += role.getColor() + "[" + role.getTag() + "] " + ChatColor.RESET;
        }
        if(clan != null && clan.getTag() != null && !clan.getTag().isEmpty()) {
            format += ChatColor.AQUA + "[" +clan.getTag() + "] " + ChatColor.RESET;
        }
        format += "<" + player.getDisplayName() + ">: " + ChatColor.RESET + message;
        return format;
    }
}
