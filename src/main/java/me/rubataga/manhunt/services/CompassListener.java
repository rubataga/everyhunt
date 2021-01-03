package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CompassListener implements Listener {

    @EventHandler
    public void hunterUseTrackingCompass(PlayerInteractEvent e){
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || //if listening for OFF_HAND
                e.getItem()==null || //if no item is being held
                !TargetManager.isHunter(e.getPlayer()) || // if the event player is not a hunter
                !TrackingCompassUtils.isTrackingCompass(e.getItem())){// if the event player is not holding a Tracking Compass
                 // if the event player is clicking an entity
            return;
        }
        Player hunter = e.getPlayer();
//        RayTraceResult rayTracedEntities = e.getPlayer().getWorld().rayTraceEntities(e.getPlayer().getLocation(), e.getPlayer().getLocation().getDirection(),2.99D);
//        if(rayTracedEntities == null){
//            hunter.sendMessage("No entities!");
//            return;
//        } else {
//            if(rayTracedEntities.getHitEntity() == null){
//                hunter.sendMessage("No entities!");
//                return;
//            }
//        }
        //hunter.sendMessage("Entity detected!");
        if(TargetManager.isTrackingDeath(hunter)){ // if hunter is tracking death and target is null
            TargetManager.removeHunterTrackingDeath(hunter);
            if(TargetManager.getHunterTarget(hunter)!=null){
                Entity target = TargetManager.getHunterTarget(hunter);
                if(target instanceof Player){
                    if(TargetManager.isRunner((Player)target)){
                        hunter.sendMessage("Tracking " + ((Player) target).getDisplayName());
                        return;
                    }
                }
            }
            TargetManager.setTarget(hunter,null);
            hunter.sendMessage("Tracker reset");
        }
//        if(TargetManager.isTrackingDeath(hunter) && TargetManager.getHunterTarget(hunter)!=null){ //if hunter is tracking death and hunter has target
//            Entity target = TargetManager.getHunterTarget(hunter);
//            TargetManager.removeHunterTrackingDeath(hunter);
//            if(!(target instanceof Player) || target.isDead()){ // if target isn't player or target is dead
//                //if(TargetManager.isRunner((Player) TargetManager.getHunterTarget(hunter))){
//                TargetManager.setTarget(hunter, null); // reset target to null
//                hunter.sendMessage("Tracker reset");
//                return;
//            }
//            hunter.sendMessage("Now tracking " + ((Player) target).getDisplayName());
//            return;
//        }
        if(TargetManager.getRunners().size()==0) { // if there are no runners
            return;
        }
        int runnerIndex;  // initialize runnerIndex
        if(TargetManager.getRunners().size()==1 ||
                TargetManager.getTargets().get(hunter)==null ||
                !(TargetManager.getTargets().get(hunter) instanceof Player)){ // if there's only one runner or the hunter has no target or the target is not a player
            runnerIndex = 0; // set the runnerIndex to 0;
        } else {
            runnerIndex = TargetManager.getRunners().indexOf(TargetManager.getTargets().get(hunter)) + 1; // cycle to the next runner in the ArrayList
            if (runnerIndex == TargetManager.getRunners().size()) { // if the runnerIndex gets greater than the # of runners
                runnerIndex = 0; // set the runnerIndex back to 0
            }
        }
        Player runner = TargetManager.getRunners().get(runnerIndex); // Player runner = the runner with index runnerIndex
        TargetManager.setTarget(hunter,runner); // the hunter is set to be hunting runner
        hunter.sendMessage("Now tracking " + runner.getName());
    }

    @EventHandler
    public void onHunterInteractWithEntity(PlayerInteractEntityEvent e){
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || !TrackingCompassUtils.isTrackingCompass(e.getPlayer().getInventory().getItemInMainHand())){
            return;
        } if(!TargetManager.isHunter(e.getPlayer())){ // if player is not a hunter or item is not a Tracking Compass
            e.getPlayer().sendMessage("You are not a hunter!");
            return;
        }
        Player hunter = e.getPlayer();
        Entity target = e.getRightClicked();
        TargetManager.setTarget(hunter,target);
        TargetManager.removeHunterTrackingDeath(hunter);
        hunter.sendMessage("Now tracking " + target.getName());
    }

    @EventHandler
    public void onHunterRevive(PlayerRespawnEvent e){
        if(TargetManager.isHunter(e.getPlayer())){
            e.getPlayer().getInventory().setItemInMainHand(TrackingCompassUtils.trackingCompass());
            return;
        }
        if(TargetManager.isRunner(e.getPlayer())){
            for(Player hunter : TargetManager.getHuntersWithTarget(e.getPlayer())){
                hunter.sendMessage(e.getPlayer().getDisplayName() + " has respawned.");
            }
        }
    }

    @EventHandler
    public void onTargetDeath(EntityDeathEvent e){
        if(TargetManager.isTarget(e.getEntity())){ // if dead entity is target
            for(Player hunter : TargetManager.getHuntersWithTarget(e.getEntity())){ // for each hunter targeting the dead entity
                TargetManager.addHunterTrackingDeath(hunter); // hunter is now targeting death location and compass is not updated
                hunter.sendMessage(TargetManager.getHunterTarget(hunter) + " has died. Now tracking " + TargetManager.getHunterTarget(hunter) + "'s death location.");
                //TargetManager.setTarget(hunter,null);
            }
        }

    }
}


//    @EventHandler
//    public void onRunnerMove(PlayerMoveEvent e){
//        if(Manhunt.isRunner(e.getPlayer())){ // if runner moves
//            Player runner = e.getPlayer(); // Player runner = the event's player
//            for(Hunter hunter : Manhunt.huntersTargeting(runner)){ // for each hunter targeting the runner
//                hunter.compassTrackRunner(runner); //update their compasses to point at targetHunters
//            }
//        }
//    }