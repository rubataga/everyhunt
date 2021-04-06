package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.managers.LobbyManager;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.utils.CommandSenderMessenger;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LobbyService {

    private static final List<Player> lobbyPlayers = LobbyManager.getLobbyPlayers();

    public static void playerJoinGame(CommandSender sender, Collection<Player> players){
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        for(Player player : players){
            if(lobbyPlayers.contains(player)){
                Debugger.send(player.getName() + " is already in this game!");
                csm.povMessageSenderOnly(player," already in this game");
                return;
            }
            if(LobbyManager.gameStarted && GameCfg.playersCanMidjoin){
                String role = GameCfg.assignRoleAtMidjoin;
                if(role.equalsIgnoreCase("hunter")){
                    TrackingService.addHunterCommand(sender, Collections.singletonList(player));
                } else if(role.equalsIgnoreCase("runner")){
                    TrackingService.addRunnerCommand(sender, Collections.singletonList(player));
                }
            }
            lobbyPlayers.add(player);
            csm.povMessage(player," now added to the game!");
            Debugger.send("Added player " + player.getName());
        }
    }

}
