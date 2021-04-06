package me.rubataga.everyhunt;

import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.PortalListener;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TrackingManager;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.*;

public class GameManager implements Listener {

    public static List<Player> gamePlayers = new ArrayList<>();
    public static boolean gameStarted;
    public static JavaPlugin plugin;
    public static PluginManager pluginManager;

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
            pluginManager.registerEvents(new GameManager(),plugin);
            gamePlayers.addAll(Bukkit.getOnlinePlayers());
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
        if(gamePlayers.size()<GameCfg.minimumPlayers){
            Debugger.send("Not enough players!");
            return;
        }
        if(GameCfg.assignRolesAtStart){
            assignRoles(GameCfg.minimumRunners);
        }
        if(GameCfg.assignTargetAtStart){
            assignTargets();
        }
        pluginManager.registerEvents(new CompassListener(),plugin);
        pluginManager.registerEvents(new DeathListener(),plugin);
        pluginManager.registerEvents(new PortalListener(),plugin);
        if(!GameCfg.playersCanMidjoin && GameCfg.autoAddToGame){
            HandlerList.unregisterAll(new GameManager());
        }
        gameStarted = true;
    }

    public static void stopGame(){
        HandlerList.unregisterAll(Everyhunt.getInstance());
        for(Player player : gamePlayers){
            TrackingManager.removeAll(player);
        }
        gamePlayers.clear();
        gameStarted=false;
    }

    private static void assignRoles(int runnerCount){
        for(Player player : gamePlayers){
            if(TrackingManager.hasRole(player,RoleEnum.TARGET)){
                runnerCount -=1;
                gamePlayers.remove(player);
            }
        }
        if(runnerCount<1){
            return;
        }
        Collections.shuffle(gamePlayers);
        if(runnerCount>gamePlayers.size()){
            runnerCount = gamePlayers.size();
        }
        for(int i = 0; i < runnerCount; i++){
            Player player = gamePlayers.get(i);
            Target runner = new Target(player);
            TrackingManager.addRunner(runner);
        }
        for(int i = runnerCount; i < gamePlayers.size(); i++){
            Player player = gamePlayers.get(i);
            Hunter hunter = new Hunter(player);
            TrackingManager.addHunter(hunter);
        }
    }

    private static void assignTargets(){
        List<Target> runnerList = TrackingManager.getRunnerList();
        if(runnerList.isEmpty()){
            return;
        }
        ListIterator<Target> iterator = TrackingManager.getRunnerList().listIterator();
        boolean runnerListHasOneRunner = runnerList.size()==1;
        for(Hunter hunter : TrackingManager.getHunters().values()){
            if(!iterator.hasNext()){
                iterator = TrackingManager.getRunnerList().listIterator();
            }
            Target target = iterator.next();
            while(target.getEntity()==hunter.getEntity()){
                if(runnerListHasOneRunner){
                    Debugger.send("Hunter is runner!");
                    continue;
                }
                if(!iterator.hasNext()){
                    iterator = TrackingManager.getRunnerList().listIterator();
                }
                target = iterator.next();
            }
            hunter.setTarget(iterator.next());
        }
    }

}
