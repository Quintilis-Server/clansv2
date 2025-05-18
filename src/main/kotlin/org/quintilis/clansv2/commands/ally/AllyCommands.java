package org.quintilis.clansv2.commands.ally;

public enum AllyCommands {
    SEND("send", "/ally send <clan>"),
    ACCEPT("accept", "/ally accept <clan>"),
    REJECT("reject", "/ally reject <clan>"),
    LIST("list", "/ally list <invites|allies>");

    private final String command;
    private final String usage;

    private AllyCommands(String command, String usage) {
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
