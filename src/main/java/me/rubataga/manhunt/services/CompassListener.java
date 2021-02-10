package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.Hunter;
import me.rubataga.manhunt.models.RoleEnum;
import me.rubataga.manhunt.models.Runner;
import me.rubataga.manhunt.models.Target;

import me.rubataga.manhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class CompassListener implements Listener {

    /**
     * EventHandler for a hunter right-clicking while holding a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param e {@link PlayerInteractEvent}
     */
    @EventHandler
    public void hunterUseTrackingCompass(PlayerInteractEvent e){
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || //if listening for OFF_HAND
                e.getItem()==null || //if no item is being held
                !TargetManager.hasRole(e.getPlayer(),RoleEnum.HUNTER) || // if the event player is not a hunter
                !(TrackingCompassUtils.isTrackingCompass(e.getItem()))){// if the event player is not holding a Tracking Compass
            return;
        }
        Hunter hunter = TargetManager.getHunters().get(e.getPlayer().getUniqueId());
        Player player = e.getPlayer();
        // if hunter is tracking death
        if(hunter.isTrackingDeath()){
            hunter.setTrackingDeath(false);
            // if target is not null
            if(hunter.getTarget()!=null){
                Entity target = hunter.getTargetEntity();
                // if target is runner
                if(TargetManager.hasRole(target,RoleEnum.RUNNER)){
                    player.sendMessage("Tracking " + ((Player) target).getDisplayName() + ".");
                    hunter.updateCompass();
                    return;
                }
            }
            hunter.setTarget(null);
            player.sendMessage("Tracker reset.");
            hunter.updateCompass();
            return;
        }
        if(TargetManager.getRunners().size()==0 || // if there are no runners
                !(hunter.getTargetEntity() instanceof Player)) { // if target isn't a player
            return;
        }

        int runnerIndex = 0;
        // if there's only one runner
        if(TargetManager.getRunnerList().size()==1){
            // if the single runner is also the hunter
            if(TargetManager.getRunnerList().get(0).getEntity()==player){
                return;
            }
        // if there are multiple runners, cycle to and select the next runner
        } else {
            if(TargetManager.hasRole(hunter.getTargetEntity(),RoleEnum.RUNNER)){
                runnerIndex = TargetManager.getRunnerList().indexOf(hunter.getTarget()) + 1; // cycle to the next runner in the ArrayList
            }
            if(TargetManager.getRunnerList().get(runnerIndex).getEntity()==player){ // if the hunter is that next runner, keep going;
                runnerIndex++;
            }
            if (runnerIndex >= TargetManager.getRunners().size()) { // if the runnerIndex gets greater than the # of runners
                runnerIndex = 0; // set the runnerIndex back to 0
            }
        }
        // set the hunter's target to the selected runner
        Runner runner = TargetManager.getRunnerList().get(runnerIndex); // Player runner = the runner with index runnerIndex
        hunter.setTarget(runner); // the hunter is set to be hunting runner
        player.sendMessage("Now tracking " + runner.getEntity().getName() + ".");
        hunter.updateCompass();
    }

    /**
     * EventHandler for a hunter right-clicking an entity while holding a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param e {@link PlayerInteractEntityEvent}
     */
    @EventHandler
    public void onHunterInteractWithEntity(PlayerInteractEntityEvent e){
        //System.out.println("§bInteracted w/Entity");
        //System.out.println("§bmain hand is trackingcompass" + (e.getPlayer().getInventory().getItemInMainHand() instanceof TrackingCompass));
        // if event is firing for offhand or player isn't holding tracking compass
        //System.out.println("§bis tracking compass: " + (TrackingCompassUtils.isTrackingCompass(e.getPlayer().getInventory().getItemInMainHand())));
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) ||
                (!TrackingCompassUtils.isTrackingCompass(e.getPlayer().getInventory().getItemInMainHand()))){
            return;
        // if player is not a hunter
        }
        if(!TargetManager.hasRole(e.getPlayer(),RoleEnum.HUNTER)){
            e.getPlayer().sendMessage("You are not a hunter!");
            return;
        }
        // set the player's target to the clicked entity
        Hunter hunter = TargetManager.getHunters().get(e.getPlayer().getUniqueId());
        // if hunter is already tracking the clicked entity
        if(hunter.getTargetEntity()==e.getRightClicked()){
            return;
        }
        Target target;
        // if clicked entity is a target, set target to the clicked entity
        if(TargetManager.hasRole(e.getRightClicked(),RoleEnum.TARGET)){
            target = TargetManager.getTargets().get(e.getRightClicked().getUniqueId());
        // create a new target
        } else {
            target = new Target(e.getRightClicked());
            TargetManager.addTarget(target);
        }
        hunter.setTarget(target);
        hunter.updateCompass(); // this is messed up
        hunter.getEntity().sendMessage("Now tracking " + target.getEntity().getName() + ".");

    }

    /**
     * EventHandler for a hunter or runner respawning
     *
     * @param e {@link PlayerRespawnEvent}
     */
    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e){
        // if respawning player is hunter, give trackingCompass
        if(TargetManager.hasRole(e.getPlayer(),RoleEnum.HUNTER)){
            // initialize the hunter object
            Hunter hunter = TargetManager.getHunters().get(e.getPlayer().getUniqueId());
            // if hunter has no compass, give them a new tracking compass
            if(!TrackingCompassUtils.hasTrackingCompass(hunter.getEntity())){
                if(hunter.getCompass()!=null){
                    hunter.setCompass(TrackingCompassUtils.trackingCompass());
                    // if hunter DOES have a compass, but its not in their invenotry, give them the same compass
                } else {
                    hunter.getEntity().getInventory().addItem(hunter.getCompass());
                }
            }
            hunter.updateCompass();
            return;
        }
        // if respawning player is runner, notify the hunter
        if(TargetManager.hasRole(e.getPlayer(),RoleEnum.RUNNER)){
            Runner runner = TargetManager.getRunners().get(e.getPlayer().getUniqueId());
            for(Hunter hunter : runner.getHunters()){
                hunter.getEntity().sendMessage(e.getPlayer().getDisplayName() + " has respawned."); // is using Bukkit faster than my map?
            }
        }
    }

    @EventHandler
    public void onTrackingCompassPickup(EntityPickupItemEvent e){
        // if item isn't TrackingCompass, ignore
        if(!TrackingCompassUtils.isTrackingCompass(e.getItem().getItemStack())){
            return;
        }
        e.setCancelled(true);
        // if hunter has compass or player isn't hunter, cancel
        if(TargetManager.hasRole(e.getEntity().getUniqueId(), RoleEnum.HUNTER)) {
            Hunter hunter = TargetManager.getHunters().get(e.getEntity().getUniqueId());
            //if player inventory doesn't have a tracking compass, give one
            if(!TrackingCompassUtils.hasTrackingCompass((Player)e.getEntity())) {
                hunter.setCompass(TrackingCompassUtils.trackingCompass());
            }
        }
    }

    /**
     * EventHandler for a target dying
     *
     * @param e {@link EntityDeathEvent}
     */
    @EventHandler
    public void onTargetDeath(EntityDeathEvent e){
        //System.out.println("BUGCHECK Entity died: " + e.getEntity().getName());
        //if a target dies, notify the player and set their target to the target's death location
        if(TargetManager.hasRole(e.getEntity(),RoleEnum.TARGET)){ // if dead entity is target
            //System.out.println("role works");
            for(Hunter hunter: TargetManager.getTargets().get(e.getEntity().getUniqueId()).getHunters()){ // for each hunter targeting the dead entity
                //System.out.println("Hunter found: " + hunter.getEntity().getName());
                hunter.setTrackingDeath(true); // hunter is now targeting death location and compass is not updated
                hunter.getEntity().sendMessage(hunter.getTargetEntity().getName() + " has died. Now tracking " + hunter.getTargetEntity().getName() + "'s death location.");
                hunter.updateCompass();
            }
        }
    }

    @EventHandler
    public void onTargetPortal(EntityPortalEnterEvent e){
        if(TargetManager.hasRole(e.getEntity(),RoleEnum.TARGET)) { // if teleporting entity is target
            for(Hunter hunter : TargetManager.getTargets().get(e.getEntity().getUniqueId()).getHunters()){
                hunter.setTrackingPortal(hunter.getEntity().getWorld().equals(e.getEntity().getWorld())); // hunter is now targeting portal location and compass is not updated
                hunter.updateCompass();
            }
        }
    }



}