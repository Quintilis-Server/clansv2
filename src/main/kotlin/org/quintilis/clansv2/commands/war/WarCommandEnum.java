package org.quintilis.clansv2.commands.war;

public enum WarCommandEnum {
    DECLARE("declare", "/war declare <clan>"),;

    private final String command;
    private final String usage;

    private WarCommandEnum(String command, String usage) {
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
