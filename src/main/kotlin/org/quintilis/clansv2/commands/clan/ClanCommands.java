package org.quintilis.clansv2.commands.clan;

public enum ClanCommands {
    CREATE("create", "/clan create <name> <tag>"),
    DELETE("delete", "/clan delete"),
    LIST("list", "/clan list"),;

    private final String command;
    private final String usage;

    private ClanCommands(String command, String usage) {
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
