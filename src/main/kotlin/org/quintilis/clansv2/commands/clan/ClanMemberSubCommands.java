package org.quintilis.clansv2.commands.clan;

import org.quintilis.clansv2.commands.CommandInterface;

public enum ClanMemberSubCommands implements CommandInterface {
    KICK("kick", "/clan member kick <player>"),
    INVITE("invite", "/clan member invite <player>"),
    LIST("list", "/clan member list");

    private final String command;
    private final String usage;

    ClanMemberSubCommands(String command, String usage) {
        this.command = command;
        this.usage = usage;
    }

    public String getCommand() {
        return this.command;
    }
    public String getUsage() {
        return this.usage;
    }
}
