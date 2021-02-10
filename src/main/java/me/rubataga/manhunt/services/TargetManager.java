package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.*;
import org.bukkit.entity.Entity;

import java.util.*;

public class TargetManager {

    private static final Map<UUID, Hunter> hunters = new HashMap<>();
    private static final Map<UUID, Target> targets = new HashMap<>();
    private static final Map<UUID, Runner> runners = new HashMap<>();
    private static final List<Runner> runnerList = new LinkedList<>();

//    public static GameEntity getGameEntity(UUID id, RoleEnum role){
//        if(role==RoleEnum.HUNTER){
//            return hunters.get(id);
//        } else if (role==RoleEnum.TARGET){
//            return targets.get(id);
//        } else if (role==RoleEnum.RUNNER){
//            return runners.get(id);
//        }
//        return null;
//    }

    public static boolean hasRole(UUID id, RoleEnum role){ // is this implementation worth it???
        if(role.equals(RoleEnum.HUNTER)){
            return hunters.containsKey(id);
        } else if (role.equals(RoleEnum.TARGET)){
            return targets.containsKey(id);
        } else if (role.equals(RoleEnum.RUNNER)){
            return runners.containsKey(id);
        }
        return false;
    }

    public static boolean hasRole(Entity entity, RoleEnum role){
        return hasRole(entity.getUniqueId(),role);
    }

    public static void addHunter(Hunter hunter){
        hunters.put(hunter.getEntity().getUniqueId(), hunter);
    }

    public static void removeHunter(UUID id){
        hunters.remove(id);
    }

    public static void addTarget(Target target){
        targets.put(target.getEntity().getUniqueId(), target);
    }

    public static void removeTarget(UUID id){
        targets.remove(id);
    }

    public static void addRunner(Runner runner){
        runners.put(runner.getEntity().getUniqueId(), runner);
        runnerList.add(runner);
    }

    public static void removeRunner(UUID id){
        runners.remove(id);
        runnerList.remove(runners.get(id));
    }

    public static Map<UUID, Hunter> getHunters() {
        return hunters;
    }

    public static Map<UUID, Target> getTargets() {
        return targets;
    }

    public static Map<UUID, Runner> getRunners() {
        return runners;
    }

    public static List<Runner> getRunnerList() {
        return runnerList;
    }

}
