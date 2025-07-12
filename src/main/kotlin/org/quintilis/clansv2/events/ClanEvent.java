package org.quintilis.clansv2.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.quintilis.clansv2.entities.ClanEntity;

public class ClanEvent extends Event {
    static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    private final ClanEntity clan;

    public ClanEvent(Player player, ClanEntity clan) {
        this.player = player;
        this.clan = clan;
    }

    public Player getPlayer() {
        return player;
    }

    public ClanEntity getClan() {
        return clan;
    }

    @NotNull
    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
