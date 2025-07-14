package org.quintilis.clansv2.luckperms;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum LuckPermsBaseRoles {
    MEMBER("Member", "", ChatColor.RESET, Arrays.asList(
            "clansv2.use"
    )),
    OWNER("Dono", "", ChatColor.GRAY, Arrays.asList(
            "clansv2.use",
            "clansv2.owner"
    )),
    MOD("Moderation", "MOD", ChatColor.AQUA, Arrays.asList(
            "clansv2.use",
            "clansv2.mod",
            "luckperms.user.info"
    )),
    ADM("Administration", "ADM", ChatColor.RED, Arrays.asList(
            "clansv2.use",
            "clansv2.mod",
            "clansv2.admin",
            "luckperms.user.permission.set",
            "luckperms.group.info"
    ));

    private final String roleName;
    private final String tag;
    private final ChatColor color;
    private final List<String> permissions;

    LuckPermsBaseRoles(String roleName, String tag, ChatColor color, List<String> permissions) {
        this.roleName = roleName;
        this.tag = tag;
        this.color = color;
        this.permissions = permissions;
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
    public List<String> getPermissions() {
        return permissions;
    }
}
