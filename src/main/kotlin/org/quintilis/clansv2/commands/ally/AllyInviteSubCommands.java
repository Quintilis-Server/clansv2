package org.quintilis.clansv2.commands.ally;

import org.quintilis.clansv2.commands.CommandInterface;

public enum AllyInviteSubCommands implements CommandInterface {
    ACCEPT("accept", "/ally invite accept <clan>"),
    REJECT("reject", "/ally invite reject <clan>"),
    SEND("send", "/ally invite send <clan>"),
    LIST("list", "/ally invite list"),;
    
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
