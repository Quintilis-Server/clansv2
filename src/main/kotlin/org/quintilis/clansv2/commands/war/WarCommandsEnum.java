package org.quintilis.clansv2.commands.war;

import org.quintilis.clansv2.commands.CommandInterface;

public enum WarCommandsEnum implements CommandInterface {
    DECLARE("declare", "/war declare <clan>"),;

    private final String command;
    private final String usage;

    private WarCommandsEnum(String command, String usage) {
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
