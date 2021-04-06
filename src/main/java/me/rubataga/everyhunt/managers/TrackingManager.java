package me.rubataga.everyhunt.managers;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class TrackingManager {

    private static final Map<Entity, Hunter> hunters = new HashMap<>();
    private static final Map<Entity, Target> targets = new HashMap<>();
    private static final Map<Entity, Target> runners = new HashMap<>();
    private static final Map<RoleEnum,Map<Entity,?>> roleMaps = new HashMap<>();

    private static final List<Target> runnerList = new LinkedList<>();

    static{
        roleMaps.put(RoleEnum.HUNTER,hunters);
        roleMaps.put(RoleEnum.TARGET,targets);
        roleMaps.put(RoleEnum.RUNNER,runners);
    }

    public static boolean hasRole(Entity entity, RoleEnum role) {
        return roleMaps.get(role).containsKey(entity);
    }

    public static List<RoleEnum> getRoles(Entity entity){
        List<RoleEnum> roles = new ArrayList<>();
        for(RoleEnum role : roleMaps.keySet()){
            if(hasRole(entity,role)){
                roles.add(role);
            }
        }
        return roles;
    }

    public static void addHunter(Player playerEntity, Hunter hunter){
        hunters.put(playerEntity, hunter);
    }

    public static void removeHunter(Entity hunter){
        hunters.remove(hunter);
    }

    public static void addTarget(Entity targetEntity, Target target){
        targets.put(targetEntity, target);
    }

    public static void removeTarget(Entity target){
//        if(GameCfg.autoRemoveRunners){
//            if(runners.containsKey(target)){
//                runners.remove(target);
//                runnerList.remove(getTarget(target));
//            }
//        }
        targets.remove(target);
    }

    public static void addRunner(Entity runnerEntity, Target runner){
        runners.put(runnerEntity, runner);
        runnerList.add(runner);
    }

    public static void removeRunner(Entity runner){
        runners.remove(runner);
        runnerList.remove(runners.get(runner));
    }

    public static void removeAll(Entity entity){
        removeHunter(entity);
        removeRunner(entity);
        removeTarget(entity);
    }

    public static Map<Entity, Hunter> getHunters() {
        return hunters;
    }

    public static Hunter getHunter(Entity entity){
        return hunters.getOrDefault(entity,null);
    }

    public static Map<Entity, Target> getTargets() {
        return targets;
    }

    public static Target getTarget(Entity entity){
        return targets.getOrDefault(entity,null);
    }

    public static Map<Entity, Target> getRunners() {
        return runners;
    }

    public static Target getRunner(Entity entity){
        return runners.getOrDefault(entity,null);
    }

    public static List<Target> getRunnerList() {
        return runnerList;
    }

    public static Map<RoleEnum,Map<Entity,?>> getRoleMaps() { return roleMaps; }

}
