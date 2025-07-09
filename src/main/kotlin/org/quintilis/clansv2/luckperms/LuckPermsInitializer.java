package org.quintilis.clansv2.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.quintilis.clansv2.Clansv2;
import org.quintilis.clansv2.managers.ClanManager;

import java.util.Objects;
import java.util.logging.Logger;

public class LuckPermsInitializer {
    private final LuckPerms luckPerms;
    private final GroupManager groupManager;
    private final Logger logger;

    public LuckPermsInitializer(Clansv2 clansv2, LuckPerms luckPerms) {
        this.logger = clansv2.getLogger();
        this.luckPerms = luckPerms;
        this.groupManager = luckPerms.getGroupManager();
    }

    public void initialize() {
        this.initalizeDefaultRoles();
        this.initiateClansGroups();
    }

    private void initalizeDefaultRoles(){
        this.logger.info("Initializing LuckPerms roles");

        for(LuckPermsBaseRoles role : LuckPermsBaseRoles.values()){
            String roleGroupName = role.name();

            Group group = this.checkAndSaveGroup(roleGroupName);
            if(!role.getTag().isEmpty()){
                Node prefixNode = PrefixNode.builder(role.getTag(), 100).build();
                group.data().add(prefixNode);
            }
            switch (role){
                case PLAYER:
                    Node groupPermissions = PermissionNode.builder("clansv2.use").build();
                    group.data().add(groupPermissions);
                    break;
                case MOD:
                    Node inheritancePlayer = InheritanceNode.builder("PLAYER").build();
                    group.data().add(inheritancePlayer);

                    group.data().add(PermissionNode.builder("clansv2.mod").build());
                    break;
                case ADM:
                    Node inheritancePlayer2 = InheritanceNode.builder("PLAYER").build();
                    group.data().add(inheritancePlayer2);

                    group.data().add(PermissionNode.builder("clansv2.admin").build());
                    group.data().add(PermissionNode.builder("luckperms.group.info").build());
                    group.data().add(PermissionNode.builder("luckperms.user.permission.set").build());
                    break;
                default:
                    break;
            }
            this.groupManager.saveGroup(group);
        }
    }


    public void initiateClansGroups(){
        this.logger.info("Initializing LuckPerms clans groups");
        ClanManager.INSTANCE.getAllClans().forEach(clan -> {
            String clanGroupName = clan.getName();
            Group group = this.checkAndSaveGroup(clanGroupName);
            Node groupPermissions = PermissionNode.builder("clansv2.use").build();
            Node prefixNode;
            if(clan.getTag().isEmpty()){
                prefixNode = PrefixNode.builder("", 100).build();
            }else{
                prefixNode = PrefixNode.builder(clan.getTag(), 50).build();

            }

            group.data().add(groupPermissions);
            group.data().add(prefixNode);

            this.groupManager.saveGroup(group);
        });
    }

    private Group checkAndSaveGroup(String clanGroupName){
        Group group = this.groupManager.getGroup(clanGroupName);
        if(group == null) {
            group = this.groupManager.createAndLoadGroup(clanGroupName).join();
        }else{
            logger.info(String.format("Group %s already exists", clanGroupName));
        }
        return group;
    }
}
