package org.quintilis.clansv2.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.quintilis.clansv2.entities.ClanEntity;

public abstract class ClanEvent extends Event {
    protected static final HandlerList HANDLERS = new HandlerList();

    protected Player player;

    protected ClanEntity clan;
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

    public abstract HandlerList getHandlers();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
