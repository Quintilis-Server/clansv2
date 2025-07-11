package org.quintilis.clansv2.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import org.quintilis.clansv2.entities.ClanEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LuckPermsManager {
    private LuckPerms luckPerms;

    public LuckPermsManager(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
    }

    public void createGroupFromClan(ClanEntity clan){
        Node prefixNode = null;
        if(clan.getTag() != null || !clan.getTag().isEmpty()){
            prefixNode = PrefixNode.builder(clan.getTag(), 50).build();
        }

        Group group = luckPerms.getGroupManager().createAndLoadGroup(clan.getName()).join();

        assert prefixNode != null;
        group.data().add(prefixNode);

        for(PermissionNode node : this.createPermissionNodes(LuckPermsBaseRoles.PLAYER)){
            group.data().add(node);
        }
    }


    public List<PermissionNode> createPermissionNodes(LuckPermsBaseRoles role) {
        return role.getPermissions().stream()
                .map(PermissionNode::builder)
                .map(PermissionNode.Builder::build)
                .collect(Collectors.toList());
    }

    public InheritanceNode createInheritanceNode(LuckPermsBaseRoles role){
        return InheritanceNode.builder(role.getRoleName()).build();
    }

}
