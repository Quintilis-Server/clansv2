package org.quintilis.clansv2.commands.clan;

import org.quintilis.clansv2.commands.CommandInterface;

public enum ClanCommands implements CommandInterface {
    CREATE("create", "/clan create <name> <tag>"),
    DELETE("delete", "/clan delete"),
    LIST("list", "/clan list"),
    SET("set", "/clan set <field> <value>"),
    MEMBER("member", "/clan member <invite|kick|list>"),
    QUIT("quit", "/clan quit");

    private final String command;
    private final String usage;

    ClanCommands(String command, String usage) {
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
