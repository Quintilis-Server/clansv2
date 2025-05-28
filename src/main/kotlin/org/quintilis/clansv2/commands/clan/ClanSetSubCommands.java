package org.quintilis.clansv2.commands.clan;

public enum ClanSetSubCommands {
    TAG("tag","/clan set tag <tag>"),
    NAME("name","/clan set name <name>"),;
    
    private final String command;
    private final String usage;
    
    ClanSetSubCommands(String command, String usage) {
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
