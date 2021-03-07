package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;

import org.bukkit.entity.Entity;

import java.util.*;

public class TargetManager {

    private static final Map<Entity, Hunter> hunters = new HashMap<>();
    private static final Map<Entity, Target> targets = new HashMap<>();
    private static final Map<Entity, Target> runners = new HashMap<>();
    private static final Map<RoleEnum,Map> roleMaps = new HashMap<>();

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

    public static void addHunter(Hunter hunter){
        hunters.put(hunter.getEntity(), hunter);
    }

    public static void removeHunter(Entity hunter){
        hunters.remove(hunter);
    }

    public static void addTarget(Target target){
        Entity targetEntity = target.getEntity();
        targets.put(targetEntity, target);
        if(GameCfg.autoAddRunners){
            if(runnerList.contains(target)){
                runners.put(targetEntity,target);
                runnerList.add(target);
            }
        }
    }

    public static void removeTarget(Entity target){
        if(GameCfg.autoRemoveRunners){
            if(runnerList.contains(target)){
                runners.remove(target);
                runnerList.remove(getTarget(target));
            }
        }
        targets.remove(target);
    }

    public static void addRunner(Target runner){
        runners.put(runner.getEntity(), runner);
        runnerList.add(runner);
    }

    public static void removeRunner(Entity runner){
        runners.remove(runner);
        runnerList.remove(runners.get(runner));
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

    public static Target getRunnerIndex(int i){
        return runnerList.get(i);
    }

}
