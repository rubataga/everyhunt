package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.GameManager;
import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class GameManagerService {

    private static final List<Player> gamePlayers = GameManager.gamePlayers;

    public static void playerJoinGame(CommandSender sender, Collection<Player> players){
        for(Player player : players){
            if(GameManager.gamePlayers.contains(player)){
                Debugger.send(player.getName() + " is already in this game!");
                return;
            }
            if(GameManager.gameStarted && GameCfg.playersCanMidjoin){
                String role = GameCfg.assignRoleAtMidjoin;
                if(role.equalsIgnoreCase("hunter")){
                    Hunter hunter = new Hunter(player);
                    TrackingManager.addHunter(hunter);
                } else if(role.equalsIgnoreCase("runner")){
                    Target runner = new Target(player,false);
                    TrackingManager.addRunner(runner);
                }
            }
            gamePlayers.add(player);
            Debugger.send("Added player " + player.getName());
        }
    }

}
