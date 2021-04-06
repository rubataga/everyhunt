package me.rubataga.everyhunt.managers;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LobbyManager implements Listener {

    private static final List<Player> lobbyPlayers = new ArrayList<>();
    public static boolean gameStarted;
    public static JavaPlugin plugin;
    public static PluginManager pluginManager;

    public static void initialize(){
        plugin = Everyhunt.getInstance();
        pluginManager = Everyhunt.getPluginManager();
    }

    public static void setRoles(){
        if(GameCfg.assignRolesAtStart){
            assignRoles(GameCfg.minimumRunners);
        }
        if(GameCfg.assignTargetAtStart){
            assignTargets();
        }
    }

    public static List<Player> getLobbyPlayers(){
        return lobbyPlayers;
    }

    private static void assignRoles(int runnerCount){
        List<Player> tempLobbyPlayers = new ArrayList<>();
        for(Player player : lobbyPlayers){
            if(TrackingManager.hasRole(player,RoleEnum.TARGET)){
                runnerCount -=1;
                //lobbyPlayers.remove(player);
            } else {
                tempLobbyPlayers.add(player);
            }
        }
        if(runnerCount<1){
            return;
        }
        Collections.shuffle(tempLobbyPlayers);
        if(runnerCount > tempLobbyPlayers.size()){
            runnerCount = tempLobbyPlayers.size();
        }
        for(int i = 0; i < runnerCount; i++){
            Player player = tempLobbyPlayers.get(i);
            try{
                TrackingService.addRunner(player);
            } catch (EntityHasRoleException ignored){
            }
        }
        for(int i = runnerCount; i < tempLobbyPlayers.size(); i++){
            Player player = tempLobbyPlayers.get(i);
            try{
                TrackingService.addHunter(player);
            } catch (EntityHasRoleException ignored){
            }
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
