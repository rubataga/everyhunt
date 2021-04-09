package me.rubataga.everyhunt.engines;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.events.GameStartEvent;
import me.rubataga.everyhunt.events.RunnerDeathEvent;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.ServerTrafficListener;
import me.rubataga.everyhunt.listeners.TeleportListener;
import me.rubataga.everyhunt.managers.GameManager;
import me.rubataga.everyhunt.managers.LobbyManager;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ClassicEngine implements Engine, Listener{

//    private final JavaPlugin PLUGIN;
    private final List<Listener> listeners = new ArrayList<>();
    private final Map<Entity,Target> dragonHunterMap = new HashMap<>();

//    public ClassicEngine(){
//        this.PLUGIN = plugin;
//    }

    @Override
    public void initialize(){
        setListeners();
    }

    @Override
    public void setListeners() {
        listeners.add(new CompassListener());
        listeners.add(new DeathListener());
        listeners.add(new TeleportListener());
        listeners.add(new ServerTrafficListener());
        listeners.add(this);
    }

    @Override
    public List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public void registerListeners(JavaPlugin plugin){
        for(Listener listener : listeners){
            Debugger.send("Registering listener: " + listener);
            PM.registerEvents(listener,plugin);
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent e){
//        players = LobbyManager.getLobbyPlayers();
    }

    @EventHandler
    public void onRunnerDeath(RunnerDeathEvent e){
        Player runnerPlayer = (Player) e.getRunner().getEntity();
        if(GameCfg.runnersReviveAsSpectator){
            runnerPlayer.setGameMode(GameMode.SPECTATOR);
        }
        runnerPlayer.sendMessage("You have died!");
    }

    @EventHandler
    public void onEnderDragonDeath(EnderDragonChangePhaseEvent e){
        Entity dragon = e.getEntity();
        if(e.getNewPhase()== EnderDragon.Phase.DYING){
            anypercentWon(dragonHunterMap.getOrDefault(dragon, null));
        }
    }

    private static void anypercentWon(Target target){
        StringBuilder victoryTextBuilder = new StringBuilder("The Runners win!");
        if (target!=null){
            victoryTextBuilder.insert(0,target).append(" has killed the Ender Dragon! ");
        }
        String victoryText = victoryTextBuilder.toString();
        for(Entity hunter : TrackingManager.getHunters().keySet()){
            hunter.sendMessage(victoryText);
        }
    }

    @EventHandler
    public void onRunnerKillEnderDragon(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        Entity dragon = e.getEntity();
        if(!TrackingManager.hasRole(damager,RoleEnum.RUNNER) || dragon.getType()!= EntityType.ENDER_DRAGON){
            return;
        }
        if(dragon.isDead()){
            dragonHunterMap.put(dragon, TrackingManager.getTarget(damager));
        }
    }

    @Override
    public void setRoles(){
        if(GameCfg.assignRolesAtStart){
            assignRoles(GameCfg.minimumRunners);
        }
        if(GameCfg.assignTargetAtStart){
            assignTargets();
        }
    }

    public void assignRoles(int runnerCount){
        List<Player> tempLobbyPlayers = new ArrayList<>();
        for(Player player : LobbyManager.getLobbyPlayers()){
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

    public void assignTargets(){
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
