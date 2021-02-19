package me.rubataga.manhunt.listeners;

import me.rubataga.manhunt.roles.Hunter;
import me.rubataga.manhunt.roles.RoleEnum;
import me.rubataga.manhunt.roles.Target;
import me.rubataga.manhunt.services.TargetManager;
import me.rubataga.manhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CompassListener implements Listener {

    /**
     * EventHandler for a hunter right-clicking while holding a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param e {@link PlayerInteractEvent}
     */
    @EventHandler
    public void hunterUseTrackingCompass(PlayerInteractEvent e){

        if(e.getAction()== Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK || e.getAction()==Action.PHYSICAL){
            return;
        }
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || //if listening for OFF_HAND
                e.getItem()==null || //if no item is being held
                !TargetManager.hasRole(e.getPlayer(), RoleEnum.HUNTER) || // if the event player is not a hunter
                !(TrackingCompassUtils.isTrackingCompass(e.getItem()))){// if the event player is not holding a Tracking Compass
            return;
        }
        // cancel the normal interact event
        e.setCancelled(true);
        Hunter hunter = TargetManager.getHunters().get(e.getPlayer());
        Player player = e.getPlayer();
        if(hunter.getEntity().isSneaking()){
            hunter.getGUI().show();
            return;
        }
        if(hunter.isLocked()){
            return;
        }
        // if hunter is tracking dead runner, queue hunter to track runner when they revive
        if(hunter.isTrackingDeath()){
            if(hunter.getTarget()!=null) {
                if (TargetManager.getRunners().containsKey(hunter.getTarget())){
                    Target runner = hunter.getTarget();
                    player.sendMessage("Tracking " + runner.getEntity().getName() + "'s death location until they revive.");
                    return;
                }
            }
            hunter.setTrackingDeath(false);
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
                runnerIndex = TargetManager.getRunnerList().indexOf(TargetManager.getRunners().get(hunter.getTargetEntity())) + 1; // cycle to the next runner in the ArrayList
            }
            if(TargetManager.getRunnerList().get(runnerIndex).getEntity()==player){ // if the hunter is that next runner, keep going;
                runnerIndex++;
            }
            if (runnerIndex >= TargetManager.getRunners().size()) { // if the runnerIndex gets greater than the # of runners
                runnerIndex = 0; // set the runnerIndex back to 0
            }
        }
        // set the hunter's target to the selected runner
        Target runner = TargetManager.getRunnerList().get(runnerIndex); // Player runner = the runner with index runnerIndex
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
            //System.out.println("BUGCHECK returned for offhand");
            return;
            // if player is not a hunter
        }
        e.setCancelled(true);
        if(!TargetManager.hasRole(e.getPlayer(),RoleEnum.HUNTER)){
            e.getPlayer().sendMessage("You are not a hunter!");
            //System.out.println("BUGCHECK returned for not hunter");
            return;
        }
        // set the player's target to the clicked entity
        Hunter hunter = TargetManager.getHunters().get(e.getPlayer());
        if(hunter.getEntity().isSneaking()){
            //System.out.println("BUGCHECK gui for sneaking");
            return;
        }
        if(hunter.isLocked()){
            //System.out.println("BUGCHECK returned for locked");
            e.setCancelled(false);
            return;
        }
        // if hunter is already tracking the clicked entity
        if(hunter.getTargetEntity()==e.getRightClicked()){
            //System.out.println("BUGCHECK returned for already tracking right clicked");
            return;
        }
        Target target;
        // if clicked entity is a target, set target to the clicked entity
        if(TargetManager.hasRole(e.getRightClicked(),RoleEnum.TARGET)){
            target = TargetManager.getTargets().get(e.getRightClicked());
            //System.out.println("BUGCHECK existing target: " + target.getEntity().getName());
            // create a new target
        } else {
            target = new Target(e.getRightClicked());
            //System.out.println("BUGCHECK new target: " + target.getEntity().getName());
            TargetManager.addTarget(target);
        }
        hunter.setTarget(target);
        hunter.updateCompass();
        hunter.getEntity().sendMessage("Now tracking " + target.getEntity().getName() + ".");
        //System.out.println("Hunter object name: " + hunter.getTargetEntity().getName());
    }

    @EventHandler
    public void onTrackingCompassPickup(EntityPickupItemEvent e){
        // if item isn't TrackingCompass, return
        if(!TrackingCompassUtils.isTrackingCompass(e.getItem().getItemStack())){
            return;
        }
        // if not a hunter, cancel and return
        if(!TargetManager.hasRole(e.getEntity(), RoleEnum.HUNTER)){
            e.setCancelled(true);
            return;
        }
        Hunter hunter = TargetManager.getHunters().get(e.getEntity());
        //if player inventory has tracking compass in inventory, cancel
        if (!hunter.inventoryHasCompass()) {
            // give hunter new compass and destroy event's item
            TrackingCompassUtils.assignTrackingCompass(hunter);
            e.getItem().remove();
            hunter.updateCompass();
        }
        e.setCancelled(true);
    }


    // when player gets into a bed and their target is null, update their compass
    @EventHandler
    public void onRespawnSetBed(PlayerBedLeaveEvent e){
        if(TargetManager.hasRole(e.getPlayer(),RoleEnum.HUNTER)){
            Hunter hunter = TargetManager.getHunters().get(e.getPlayer());
            if(hunter.getTarget()==null){
                hunter.updateCompass();
            }
        }
    }

}
