package org.quintilis.clansv2.commands.clan;

public enum ClanCommands {
    CREATE("create"),
    DELETE("delete"),
    LIST("list");

    private final String command;

    private ClanCommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
