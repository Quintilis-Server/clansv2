package org.quintilis.clansv2.luckperms;

import org.bukkit.ChatColor;

public enum LuckPermsBaseRoles {
    PLAYER("Player", "", ChatColor.RESET),
    MOD("Moderation", "MOD", ChatColor.AQUA),
    ADM("Administration", "ADM", ChatColor.RED);

    private final String roleName;
    private final String tag;
    private final ChatColor color;

    LuckPermsBaseRoles(String roleName, String tag, ChatColor color) {
        this.roleName = roleName;
        this.tag = tag;
        this.color = color;
    }

    public String getRoleName() {
        return roleName;
    }
    public String getTag() {
        return tag;
    }
    public ChatColor getColor() {
        return color;
    }
}
