package me.rubataga.manhunt.services;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class TargetManager {

    private static HashMap<Player, Entity> targets = new HashMap<>();
    private static List<Player> runners = new LinkedList<>();
    private static List<Player> huntersTrackingDeath = new LinkedList<>();

    public static HashMap<Player, Entity> getTargets() {
        return targets;
    }
    public static List<Player> getRunners() { return runners; }
    public static List<Player> getHuntersTrackingDeath() { return huntersTrackingDeath; }

    public static Entity getHunterTarget(Player hunter){
        return targets.get(hunter);
    }

    public static void setTarget(Player hunter, Entity target){
        if(targets.replace(hunter,target)==null){
            targets.put(hunter,target);
        }
        if(target instanceof Player){
            if(!runners.contains(target)){
                runners.add((Player)target);
            }
        }

    }

    public static void addHunterTrackingDeath(Player hunter){
        huntersTrackingDeath.add(hunter);
    }
    public static void removeHunterTrackingDeath(Player hunter){
        huntersTrackingDeath.remove(hunter);
    }

    public static void removeHunter(Player hunter){
        targets.remove(hunter);
    }

    public static void removeTarget(Entity target){
        if(targets.containsValue(target)){
            for (Map.Entry<Player, Entity> playerEntityEntry : targets.entrySet()) {
                if (((Map.Entry) playerEntityEntry).getValue().equals(target)) {
                    ((Map.Entry) playerEntityEntry).setValue(null);
                }
            }
        }
    }

    public static void removeRunner(Player runner){
        if(runners.contains(runner)){
            runners.remove(runner);
        }
    }

    public static boolean isHunter(Player hunter){
        return targets.containsKey(hunter);
    }

    public static boolean isTrackingDeath(Player hunter) { return huntersTrackingDeath.contains(hunter); }

    public static boolean isRunner(Player runner){
        return runners.contains(runner);
    }

    public static boolean isTarget(Entity target){
        return targets.containsValue(target);
    }

    public static List<Player> getHuntersWithTarget(Entity entity){
        List<Player> huntersTargetingRunner = new LinkedList<>();
        for (Map.Entry<Player, Entity> playerEntityEntry : targets.entrySet()) {
            if (((Map.Entry) playerEntityEntry).getValue().equals(entity)) {
                huntersTargetingRunner.add(playerEntityEntry.getKey());
            }
        }
        return huntersTargetingRunner;
    }

}
