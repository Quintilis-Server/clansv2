package org.quintilis.clansv2.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.quintilis.clansv2.entities.PlayerEntity;
import org.quintilis.clansv2.managers.PlayerManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClansScoreboard implements Runnable{
    private Scoreboard scoreboard;

    public ClansScoreboard() {
        this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            createNewScoreboard(player);
        }
    }

    private void createNewScoreboard(Player player){
        List<PlayerEntity> players = PlayerManager.INSTANCE.getAllPlayers();
        Objective objective = scoreboard.getObjective("clans");
        if(objective == null){
            objective = scoreboard.registerNewObjective("clans", Criteria.DUMMY, ChatColor.RED + "QUINTILIS" + ChatColor.RESET);

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        }
        objective.getScore("    ").setScore(32);
        objective.getScore(ChatColor.LIGHT_PURPLE +"-- Honras de Morte --").setScore(31);
        objective.getScore("").setScore(30);

        List<PlayerEntity> playersSorted = players.stream()
                .sorted(Comparator.comparingInt(PlayerEntity::getPoints))
                .collect(Collectors.toList());
//        int max = Math.min(5, playersSorted.size());
//        for(int i = 29; i >= 29-max; i--){
//            objective.getScore(playersSorted.get(i).getName()).setScore(i);
//        }
//        System.out.println(objective.getDisplayName());
        player.setScoreboard(scoreboard);
    }
}
