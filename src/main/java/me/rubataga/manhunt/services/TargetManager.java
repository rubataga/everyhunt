package me.rubataga.manhunt.services;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Static class that stores and manages hunter/target pairs.
 */

// AP CSA: Accessor/Mutator methods (get/set)

public class TargetManager {

    private TargetManager(){}

    // AP CSA: static variables
    private static final HashMap<Player, Entity> hunterTargetMap = new HashMap<>();
    private static final List<Player> runners = new LinkedList<>();
    private static final List<Player> huntersTrackingDeath = new LinkedList<>();

    /**
     * Returns a list of all hunters of tracking an entity
     *
     * @param entity Entity to find hunters for.
     * @return a list containing players tracking the entity
     */

    // AP CSA: static methods

    public static List<Player> getHuntersWithTarget(Entity entity){
        List<Player> huntersTargetingRunner = new LinkedList<>();
        // AP CSA: For each loops
        for (Map.Entry<Player, Entity> playerEntityEntry : hunterTargetMap.entrySet()) {
            // AP CSA: Comparing objects with .equals()
            if (playerEntityEntry.getValue().equals(entity)) {
                huntersTargetingRunner.add(playerEntityEntry.getKey());
            }
        }
        return huntersTargetingRunner;
    }

    /**
     * Returns a hunter's target entity
     *
     * @param hunter player to find target for
     * @return the entity being tracked by hunter
     */

    public static Entity getHunterTarget(Player hunter){
        return hunterTargetMap.get(hunter);
    }

    /**
     * Removes a player as a hunter
     *
     * @param hunter - player to remove
     */

    public static void removeHunter(Player hunter){
        hunterTargetMap.remove(hunter);
    }

    /**
     * Checks if a player is a hunter
     *
     * @param hunter player to check if hunter
     * @return true if player is a hunter, key in hunterTargetMap
     */

    public static boolean isHunter(Player hunter){
        return hunterTargetMap.containsKey(hunter);
    }

    /**
     * Returns the map containing all hunters and targets
     *
     * @return a map containing all hunters (keys) and their targets (values)
     */

    public static HashMap<Player, Entity> getHunterTargetMap() {
        return hunterTargetMap;
    }

    /**
     * Sets a player's target
     *
     * @param hunter player to add as hunter or set target for
     * @param target hunter's new target
     */

    public static void setTarget(Player hunter, Entity target){
        if(hunterTargetMap.replace(hunter,target)==null){
            hunterTargetMap.put(hunter,target);
        }
        if(target instanceof Player){
            if(!runners.contains(target)){
                runners.add((Player)target);
            }
        }

    }

    /**
     * Removes an entity as a target
     *
     * @param target entity to remove from hunterTargetMap
     */

    public static void removeTarget(Entity target){
        if(hunterTargetMap.containsValue(target)){
            for (Map.Entry<Player, Entity> playerEntityEntry : hunterTargetMap.entrySet()) {
                if ((playerEntityEntry).getValue().equals(target)) {
                    (playerEntityEntry).setValue(null);
                }
            }
        }
    }

    /**
     * Checks if an entity is a target
     *
     * @param target entity to check
     * @return true if target is target, value in hunterTargetMap
     */

    public static boolean isTarget(Entity target){
        return hunterTargetMap.containsValue(target);
    }

    /**
     * Returns a list of runners
     *
     * @return a list containing players who are runners
     */

    public static List<Player> getRunners() { return runners; }

    /**
     * Adds a player as a runner
     *
     * @param runner player to add as runner
     */

    public static void addRunner(Player runner) { runners.add(runner); }

    /**
     * Removes a player as a runner
     *
     * @param runner player to remove as runner
     */

    public static void removeRunner(Player runner){
        runners.remove(runner);
    }

    /**
     * Checks if a player is a runner
     *
     * @param runner player to check if runner
     * @return true if player is a runner, value contained in runners
     */

    public static boolean isRunner(Player runner){
        return runners.contains(runner);
    }

    //public static List<Player> getHuntersTrackingDeath() { return huntersTrackingDeath; }

    /**
     * Adds a player to tracking the location of its target's death
     *
     * @param hunter hunter to add to huntersTrackingDeath
     */

    public static void addHunterTrackingDeath(Player hunter){
        huntersTrackingDeath.add(hunter);
    }

    /**
     * Removes a player from tracking the location of its target's death
     *
     * @param hunter hunter to remove from huntersTrackingDeath
     */

    public static void removeHunterTrackingDeath(Player hunter){
        huntersTrackingDeath.remove(hunter);
    }

    /**
     * Checks if a player is tracking the location of its target's death
     *
     * @param hunter hunter tracking a death location
     * @return true if player player is tracking a death location, value contained in huntersTrackingDeath
     */

    public static boolean isTrackingDeath(Player hunter) { return huntersTrackingDeath.contains(hunter); }

}
