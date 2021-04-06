package me.rubataga.everyhunt.managers;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.TeleportListener;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.List;

public class GameManager {

    public static boolean gameStarted;
    public static JavaPlugin plugin;
    public static PluginManager pluginManager;
    private static final List<Player> lobbyPlayers = LobbyManager.getLobbyPlayers();

    public static void initialize(){
        plugin = Everyhunt.getInstance();
        pluginManager = Everyhunt.getPluginManager();
    }

    public static void newGame(String cfg) throws FileNotFoundException {
        try{
            GameCfg.load(cfg);
        } catch (FileNotFoundException e){
            throw new FileNotFoundException("Config file " + cfg + " not found!");
        }
        newGame();
    }

    public static void newGame(){
        if(gameStarted){
            stopGame();
        }
        Debugger.send("Created new game with configuration: " + GameCfg.gameName);
        if(GameCfg.autoAddToGame){
            pluginManager.registerEvents(new LobbyManager(),plugin);
            lobbyPlayers.addAll(Bukkit.getOnlinePlayers());
        }
        if(GameCfg.autoStartOnLoad){
            startGame();
        }
    }

    public static void startGame(){
        if(gameStarted){
            Debugger.send("Game already started!");
            return;
        }
        if(lobbyPlayers.size()<GameCfg.minimumPlayers){
            Debugger.send("Not enough players!");
            return;
        }
        LobbyManager.setRoles();
        pluginManager.registerEvents(new CompassListener(),plugin);
        pluginManager.registerEvents(new DeathListener(),plugin);
        pluginManager.registerEvents(new TeleportListener(),plugin);
        if(!GameCfg.playersCanMidjoin && GameCfg.autoAddToGame){
            HandlerList.unregisterAll(new LobbyManager());
        }
        gameStarted = true;
    }

    public static void stopGame(){
        HandlerList.unregisterAll(Everyhunt.getInstance());
        for(Player player : LobbyManager.getLobbyPlayers()){
            TrackingManager.removeAll(player);
        }
        lobbyPlayers.clear();
        gameStarted=false;
    }

}
