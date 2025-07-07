package org.quintilis.clansv2.commands.ally;

import org.quintilis.clansv2.commands.CommandInterface;

public enum AllyListSubCommands implements CommandInterface {
    ALLIES("allies", "/ally list allies"),
    INVITES("invites", "/ally list invites"),;
    
    private final String command;
    private final String usage;
    
    AllyListSubCommands(String command, String usage) {
        this.command = command;
        this.usage = usage;
    }
    
    public String getCommand() {
        return command;
    }
    public String getUsage() {
        return usage;
    }
}
