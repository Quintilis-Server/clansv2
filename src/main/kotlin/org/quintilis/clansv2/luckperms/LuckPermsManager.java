package org.quintilis.clansv2.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.quintilis.clansv2.entities.ClanEntity;
import org.quintilis.clansv2.managers.PlayerManager;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class LuckPermsManager {
    private final LuckPerms luckPerms;

    public LuckPermsManager(LuckPerms luckPerms){
        this.luckPerms = luckPerms;
    }

    public void createGroupFromClan(ClanEntity clan){
        Node prefixNode = null;
        if(clan.getTag() != null && !clan.getTag().isEmpty()){
            prefixNode = PrefixNode.builder(clan.getTag(), 50).build();
        }

        Group group = luckPerms.getGroupManager().createAndLoadGroup(clan.getName()).join();

        if(prefixNode != null)
            group.data().add(prefixNode);


        for(PermissionNode node : this.createPermissionNodes(LuckPermsBaseRoles.MEMBER)){
            group.data().add(node);
        }
        luckPerms.getGroupManager().saveGroup(group);
    }

    public void deleteGroupFromClan(ClanEntity clan){
        luckPerms.getGroupManager().deleteGroup(Objects.requireNonNull(luckPerms.getGroupManager().getGroup(clan.getName())));
    }

    public void editPrefixGroupFromClan(ClanEntity clan) {
        Group group = luckPerms.getGroupManager().getGroup(clan.getName());
        if (group == null) {
            System.out.println("Group with name " + clan.getName() + " does not exist");
            return;
        }

        Node prefixNode = PrefixNode.builder(clan.getTag(), 50).build();

        group.data().clear(node -> node instanceof PrefixNode);

        group.data().add(prefixNode);

        this.luckPerms.getGroupManager().saveGroup(group);
    }

    public void editNameGroupFromClan(ClanEntity clan, String newName) {
        Group group = luckPerms.getGroupManager().getGroup(clan.getName());

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


    //Clan members functions
    public void addMembersToClan(ClanEntity clan){
        Group group = luckPerms.getGroupManager().getGroup(clan.getName());
        for(ObjectId playerId : clan.getMembers()){
            Player player = PlayerManager.INSTANCE.getMinePlayerById(playerId);
            assert player != null;
            this.addMemberToClanGroup(clan, player);
        }
    }

    public void removeMembersFromClan(ClanEntity clan){

    }

    public void addMemberToClanGroup(ClanEntity clan, Player player){
        UUID uuid = player.getUniqueId();

        this.luckPerms.getUserManager().loadUser(uuid).thenAcceptAsync(user->{
            if(user == null){
                player.sendMessage(ChatColor.RED + "User not found");
                return;
            }

            InheritanceNode node = InheritanceNode.builder(clan.getName()).build();

            DataMutateResult result = user.data().add(node);

            luckPerms.getUserManager().saveUser(user);

            if(result.wasSuccessful()){
                player.sendMessage(ChatColor.GREEN + "Você foi adicionado ao grupo " + clan.getName());
            }else {
                player.sendMessage(ChatColor.RED + "Você ja esta no grupo" + clan.getName());
            }
        });
    }

    public void removeMemberFromClanGroup(ClanEntity clan, Player player){
        UUID uuid = player.getUniqueId();

        this.luckPerms.getUserManager().loadUser(uuid).thenAcceptAsync(user->{
            if(user == null){
                player.sendMessage(ChatColor.RED + "User not found");
                return;
            }

            InheritanceNode node = InheritanceNode.builder(clan.getName()).build();
            DataMutateResult result = user.data().remove(node);

            luckPerms.getUserManager().saveUser(user);

            if(result.wasSuccessful()){
                player.sendMessage(ChatColor.GREEN + "Você foi removido do grupo " + clan.getName());
            }else {
                player.sendMessage(ChatColor.RED + "Você ja esta no grupo" + clan.getName());
            }
        });
    }
}
