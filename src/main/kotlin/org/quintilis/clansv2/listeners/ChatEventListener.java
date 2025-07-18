package org.quintilis.clansv2.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.quintilis.clansv2.entities.ClanEntity;
import org.quintilis.clansv2.managers.ClanManager;

public class ChatEventListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ClanEntity clan = ClanManager.INSTANCE.getClanByMember(player);
        if(clan != null) {
//            event.setCancelled(true);
            event.setFormat(buildChatFormat(player,clan,event.getMessage()));
        }else{
            event.setCancelled(false);
        }
    }
    private String buildChatFormat(Player player, ClanEntity clan, String message) {
        String format = "";
        if(clan.getTag() != null && !clan.getTag().isEmpty()) {
            format += ChatColor.AQUA + "[" +clan.getTag() + "]" + ChatColor.RESET;
        }
        format += " <" + player.getDisplayName() + ">: "+ message + ChatColor.RESET;
        return format;
    }
}
