package org.quintilis.clansv2.commands.ally;

import org.quintilis.clansv2.commands.CommandInterface;

public enum AllyCommands implements CommandInterface {
    INVITE("invite", "/ally invite <send|accept|reject|list> <clan>"),
    LIST("list", "/ally list"),
    REMOVE("remove", "/ally remove <clan>");

    private final String command;
    private final String usage;

    AllyCommands(String command, String usage) {
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
