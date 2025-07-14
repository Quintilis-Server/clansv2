package org.quintilis.clansv2.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.quintilis.clansv2.entities.ClanEntity;

public class ClanDeleteEvent extends ClanEvent {
    public ClanDeleteEvent(Player player, ClanEntity clan) {
        super(player, clan);
    }
}
