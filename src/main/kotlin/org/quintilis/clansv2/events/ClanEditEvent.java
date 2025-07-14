package org.quintilis.clansv2.events;

import org.bukkit.entity.Player;
import org.quintilis.clansv2.entities.ClanEntity;

public class ClanEditEvent extends ClanEvent {

    private final ClanEditEventType type;

    private final String oldName;

    public ClanEditEvent(Player player, ClanEntity clan, ClanEditEventType type) {
        super(player, clan);
        this.type = type;
        this.oldName = null;
    }

    public ClanEditEvent(Player player, ClanEntity clan, ClanEditEventType type, String oldName) {
        super(player, clan);
        this.type = type;
        this.oldName = oldName;
    }

    public ClanEditEventType getType() {
        return type;
    }

    public String getOldName() {
        return oldName;
    }
}
