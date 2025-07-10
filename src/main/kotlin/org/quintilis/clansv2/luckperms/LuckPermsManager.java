package org.quintilis.clansv2.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PrefixNode;
import org.quintilis.clansv2.entities.ClanEntity;

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

        Node permisisons

        Group group = luckPerms.getGroupManager().createAndLoadGroup(clan.getName()).join();

        assert prefixNode != null;
        group.data().add(prefixNode);

        group.data().add()
    }


}
