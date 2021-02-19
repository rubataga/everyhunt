package me.rubataga.manhunt.listeners;

import me.rubataga.manhunt.roles.Hunter;
import me.rubataga.manhunt.roles.RoleEnum;
import me.rubataga.manhunt.roles.Target;
import me.rubataga.manhunt.services.TargetManager;
import me.rubataga.manhunt.utils.TrackingCompassUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener {

    /**
     * EventHandler for a target dying
     *
     * @param e {@link EntityDeathEvent}
     */
    @EventHandler
    public void onTargetDeath(EntityDeathEvent e){
        //System.out.println("BUGCHECK Entity died: " + e.getEntity().getName());
        //if a target dies, notify the player and set their target to the target's death location
        if(TargetManager.hasRole(e.getEntity(),RoleEnum.TARGET)) { // if dead entity is target
            //System.out.println("has role");
            //System.out.println("role works");
            //System.out.println("entity hunter list size:" + TargetManager.getTargets().get(e.getEntity()).getHunters().size());
            Target target = TargetManager.getTargets().get(e.getEntity());
            target.updateLastLocation();
            for (Hunter hunter : target.getHunters()) { // for each hunter targeting the dead entity
                //System.out.println("Hunter found: " + hunter.getEntity().getName());
                hunter.setTrackingDeath(true); // hunter is now targeting death location and compass is not updated
                hunter.getEntity().sendMessage(hunter.getTargetEntity().getName() + " has died. Now tracking " + hunter.getTargetEntity().getName() + "'s death location.");
                hunter.setLastTracked(e.getEntity().getLocation());
                hunter.updateCompass();
            }
        }
    }

    /**
     * EventHandler for a hunter or runner respawning
     *
     * @param e {@link PlayerRespawnEvent}
     */
    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e){
        // if respawning player is hunter, give trackingCompass
        if(TargetManager.hasRole(e.getPlayer(), RoleEnum.HUNTER)){
            // initialize the hunter object
            Hunter hunter = TargetManager.getHunters().get(e.getPlayer());
            // assign a compass to the hunter
            TrackingCompassUtils.assignTrackingCompass(hunter);
            if(hunter.isTrackingPortal() || hunter.isTrackingDeath()){
                hunter.getEntity().setCompassTarget(hunter.getLastTracked());
            }
            hunter.updateCompass();
        }
        // if respawning player is runner, notify the hunter
        if(TargetManager.hasRole(e.getPlayer(),RoleEnum.RUNNER)){
            Target runner = TargetManager.getRunners().get(e.getPlayer());
            for(Hunter hunter : runner.getHunters()){
                hunter.setTrackingDeath(false);
                hunter.updateCompass();
                hunter.getEntity().sendMessage(e.getPlayer().getDisplayName() + " has respawned.");
            }
        }
    }

}
