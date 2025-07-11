package org.quintilis.clansv2.commands.invite;

import org.quintilis.clansv2.commands.CommandInterface;

public enum InviteCommands implements CommandInterface {
    ACCEPT("accept", "/invite accept <clan>"),
    REJECT("reject", "/invite reject <clan>"),
    LIST("list", "/invite list");

    private final String command;
    private final String usage;

    InviteCommands(String command, String usage) {
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
