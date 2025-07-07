package org.quintilis.clansv2.commands.enemy;

import org.quintilis.clansv2.commands.CommandInterface;

public enum EnemyCommands implements CommandInterface {
    LIST("list", "/enemy list"),
    REMOVE("remove", "/enemy remove <clan>");

    private final String command;
    private final String usage;

    private EnemyCommands(String command, String usage) {
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
