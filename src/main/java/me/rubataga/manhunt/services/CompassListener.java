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

    public CompassListener(){}

    @EventHandler
    public void hunterUseTrackingCompass(PlayerInteractEvent e){
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || //if listening for OFF_HAND
                e.getItem()==null || //if no item is being held
                !TargetManager.isHunter(e.getPlayer()) || // if the event player is not a hunter
                !TrackingCompassUtils.isTrackingCompass(e.getItem())){// if the event player is not holding a Tracking Compass
            return;
        }
        Player hunter = e.getPlayer();
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
        if(TargetManager.getRunners().size()==0 || // if there are no runners
                !(TargetManager.getTargets().get(hunter) instanceof Player)) { // if target isn't a player
            return;
        }
        int runnerIndex = 0;  // initialize runnerIndex
        if(TargetManager.getRunners().size()==1){  // if there's only one runner
            if(TargetManager.getRunners().get(0)==hunter){ // if the single runner is also the hunter
                return;
            }
        } else {
            runnerIndex = TargetManager.getRunners().indexOf(TargetManager.getTargets().get(hunter)) + 1; // cycle to the next runner in the ArrayList
            if(TargetManager.getRunners().get(runnerIndex)==hunter){ // if the hunter is that next runner, keep going;
                runnerIndex++;
            }
            if (runnerIndex >= TargetManager.getRunners().size()) { // if the runnerIndex gets greater than the # of runners
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
            }
        }

    }
}