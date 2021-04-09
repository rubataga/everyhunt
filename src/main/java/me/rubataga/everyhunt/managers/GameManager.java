package me.rubataga.everyhunt.managers;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.engines.Engine;
import me.rubataga.everyhunt.events.GameStartEvent;
import me.rubataga.everyhunt.events.GameStopEvent;
import me.rubataga.everyhunt.exceptions.GameStartException;

import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.List;

public class GameManager {

    private static final PluginManager PM = Bukkit.getPluginManager();
//    private static Engine engine = Everyhunt.getPluginEngine();

    public static boolean gameStarted;
    public static JavaPlugin plugin;
    private static final List<Player> lobbyPlayers = LobbyManager.getLobbyPlayers();

    public static void initialize(){
        plugin = Everyhunt.getInstance();
    }

    public static void newGame(){
        if(gameStarted){
            try {
                stopGame();
            } catch (GameStartException ignore) {}
        }
        Debugger.send("Created new game with configuration: " + GameCfg.gameName);
        if(GameCfg.autoStartOnLoad){
            try {
                startGame();
            } catch (GameStartException ignore) {}
        }
    }

    public static void startGame() throws GameStartException {
        if(gameStarted){
            throw new GameStartException("Game already started!");
        }
        if(lobbyPlayers.size()<GameCfg.minimumPlayers){
            throw new GameStartException("Not enough players!");
        }
        Engine engine = Everyhunt.getPluginEngine();
        engine.registerListeners(plugin);
        engine.setRoles();
        gameStarted = true;
        PM.callEvent(new GameStartEvent(GameCfg.gameName));
    }

    public static void stopGame() throws GameStartException {
        if(!gameStarted){
            throw new GameStartException("Game hasn't started yet!");
        }
        HandlerList.unregisterAll(Everyhunt.getInstance());
        TrackingManager.clearCollections();
        lobbyPlayers.clear();
        gameStarted=false;
        PM.callEvent(new GameStopEvent());
    }

//    public static void registerListeners(List<Listener> listeners, JavaPlugin plugin){
//        for(Listener listener : listeners){
//            PM.registerEvents(listener,plugin);
//        }
//    }

}
