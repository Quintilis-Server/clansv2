package org.quintilis.clansv2.commands.ally;

public enum AllyInviteSubCommands {
    ACCEPT("accept", "/ally invite accept <clan>"),
    REJECT("reject", "/ally invite reject <clan>"),
    SEND("send", "/ally invite send <clan>");
    
    private final String command;
    private final String usage;
    
    AllyInviteSubCommands(String command, String usage) {
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
