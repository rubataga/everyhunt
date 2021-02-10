//package me.rubataga.manhunt.services;
//
//import me.rubataga.manhunt.models.Hunter;
//import me.rubataga.manhunt.utils.TrackingCompassUtils;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Player;
//
//import java.util.*;
//
///**
// * Static class that stores and manages hunter/target pairs.
// * AP CSA: Accessor/Mutator methods (get/set)
// */
//public class TargetManagerDep {
//
//    private TargetManagerDep(){}
//
//    private static final HashMap<UUID, Hunter> hunters = new HashMap<>();
//    private static final HashMap<UUID, Entity> targets = new HashMap<>();
//
//    private static final List<Player> runners = new LinkedList<>();
//
//
//    /**
//     * Returns a list of all hunters of tracking an entity
//     *
//     * @param entity Entity to find hunters for.
//     * @return a list containing players tracking the entity
//     */
//    public static List<Player> getHuntersWithTarget(Entity entity){
//        List<Player> huntersTargetingRunner = new LinkedList<>();
//        for (Map.Entry<Player, HunterTarget> playerEntityEntry : hunterTargetMap.entrySet()) {
//            if (playerEntityEntry.getValue().getTarget().equals(entity)) {
//                huntersTargetingRunner.add(playerEntityEntry.getKey());
//            }
//        }
//        return huntersTargetingRunner;
//    }
//
//    /**
//     * Returns a hunter's target entity
//     *
//     * @param hunter player to find target for
//     * @return the entity being tracked by hunter
//     */
//    public static Entity getHunterTarget(Player hunter){
//        return hunterTargetMap.get(hunter).getTarget();
//    }
//
//    /**
//     * Removes a player as a hunter
//     *
//     * @param hunter - player to remove
//     */
//    public static void removeHunter(Player hunter){
//        hunterTargetMap.remove(hunter);
//    }
//
//    /**
//     * Checks if a player is a hunter
//     *
//     * @param hunter player to check if hunter
//     * @return true if player is a hunter, key in hunterTargetMap
//     */
//    public static boolean isHunter(Player hunter){
//        return hunterTargetMap.containsKey(hunter);
//    }
//
//
//    /**
//     * Sets a player's target
//     *
//     * @param hunter player to add as hunter or set target for
//     * @param target hunter's new target
//     */
//    public static void setTarget(Player hunter, Entity target){
//        hunterTargetMap.put(hunter,new HunterTarget(target));
//        if(target instanceof Player){
//            if(!runners.contains(target)){
//                runners.add((Player)target);
//            }
//        }
//        TargetManagerDep.runTargetUpdate(hunter);
//        if(TrackingCompassUtils.getTrackingCompass(hunter)!=null){
//            TrackingCompassUtils.runCompassUpdate(hunter);
//        }
//    }
//
//    public static void updateHunterTarget(Player hunter,Entity target){
//        hunterTargetMap.put(hunter,new HunterTarget(target));
//    }
//
//    /**
//     * Removes an entity as a target
//     *
//     * @param target entity to remove from hunterTargetMap
//     */
//    public static void removeTarget(Entity target){
//        if(hunterTargetMap.containsValue(target)){
//            for (Map.Entry<Player, Entity> playerEntityEntry : hunterTargetMap.entrySet()) {
//                if ((playerEntityEntry).getValue().equals(target)) {
//                    (playerEntityEntry).setValue(null);
//                }
//            }
//        }
//    }
//
//    /**
//     * Checks if an entity is a target
//     *
//     * @param target entity to check
//     * @return true if target is target, value in hunterTargetMap
//     */
//    public static boolean isTarget(Entity target){
//        return hunterTargetMap.containsValue(target);
//    }
//
//    /**
//     * Returns a list of runners
//     *
//     * @return a list containing players who are runners
//     */
//    public static List<Player> getRunners() { return runners; }
//
//    /**
//     * Adds a player as a runner
//     *
//     * @param runner player to add as runner
//     */
//    public static void addRunner(Player runner) { runners.add(runner); }
//
//    /**
//     * Removes a player as a runner
//     *
//     * @param runner player to remove as runner
//     */
//    public static void removeRunner(Player runner){
//        runners.remove(runner);
//    }
//
//    /**
//     * Checks if a player is a runner
//     *
//     * @param runner player to check if runner
//     * @return true if player is a runner, value contained in runners
//     */
//    public static boolean isRunner(Player runner){
//        return runners.contains(runner);
//    }
//
//    /**
//     * Adds a player to tracking the location of its target's death
//     *
//     * @param hunter hunter to add to huntersTrackingDeath
//     */
//    public static void addHunterTrackingDeath(Player hunter){
//        TrackingCompassUtils.compassUpdateTrackingDeath(hunter);
//        huntersTrackingDeath.add(hunter);
//    }
//
//    /**
//     * Removes a player from tracking the location of its target's death
//     *
//     * @param hunter hunter to remove from huntersTrackingDeath
//     */
//    public static void removeHunterTrackingDeath(Player hunter){
//        huntersTrackingDeath.remove(hunter);
//    }
//
//    /**
//     * Checks if a player is tracking the location of its target's death
//     *
//     * @param hunter hunter tracking a death location
//     * @return true if player player is tracking a death location, value contained in huntersTrackingDeath
//     */
//    public static boolean isTrackingDeath(Player hunter) { return huntersTrackingDeath.contains(hunter); }
//
//    /**
//     * Adds a player to tracking their target's last location in this world
//     *
//     * @param hunter hunter to add to huntersTrackingPortal
//     */
//    public static void addHunterTrackingPortal(Player hunter){
//        TrackingCompassUtils.compassUpdateTrackingPortal(hunter);
//        huntersTrackingPortal.add(hunter);
//    }
//
//    /**
//     * Removes a player from tracking their target's last location in this world
//     *
//     * @param hunter hunter to remove from huntersTrackingPortal
//     */
//    public static void removeHunterTrackingPortal(Player hunter){
//        huntersTrackingPortal.remove(hunter);
//    }
//
//    /**
//     * Checks if a player is tracking their target's last location in this world
//     *
//     * @param hunter hunter tracking a portal
//     * @return true if player player is tracking a portal, value contained in huntersTrackingPortal
//     */
//    public static boolean isTrackingPortal(Player hunter) { return huntersTrackingPortal.contains(hunter); }
//
//    public static void runTargetUpdate(Player hunter){
//        if(TargetManagerDep.isTrackingPortal(hunter)){
//            TargetManagerDep.removeHunterTrackingDeath(hunter);
//        } else if(TargetManagerDep.isTrackingDeath(hunter)){
//            TargetManagerDep.removeHunterTrackingPortal(hunter);
//        }
//    }
//
//}
