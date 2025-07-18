package org.quintilis.clansv2.enums;

import org.bukkit.ChatColor;

public enum UserRoles {
    ADM("Admin", "ADM", ChatColor.RED),
    PLAYER("Player", null, ChatColor.RESET),
    DEVELOPER("Developer", "DEV", ChatColor.DARK_RED);

    private final String name;
    private final String tag;
    private final ChatColor color;

    UserRoles(String name, String tag, ChatColor color) {
        this.name = name;
        this.tag = tag;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public ChatColor getColor() {
        return color;
    }
}
