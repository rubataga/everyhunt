package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        Entity entity = e.getEntity();
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) { // if dead entity is target
            //System.out.println("has role");
            //System.out.println("role works");
            //System.out.println("entity hunter list size:" + TargetManager.getTargets().get(e.getEntity()).getHunters().size());
            Target target = TargetManager.getTarget(entity);
            target.updateLastLocation();
            for (Hunter hunter : target.getHunters()) { // for each hunter targeting the dead entity
                //System.out.println("Hunter found: " + hunter.getEntity().getName());
                hunter.setTrackingDeath(true); // hunter is now targeting death location and compass is not updated
                hunter.getEntity().sendMessage(entity.getName() + " has died. Now tracking " + hunter.getTargetEntity().getName() + "'s death location.");
                hunter.setLastTracked(entity.getLocation());
                hunter.updateCompassMeta();
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
        Player player = e.getPlayer();
        if(TargetManager.hasRole(player, RoleEnum.HUNTER)){
            // initialize the hunter object
            Hunter hunter = TargetManager.getHunter(player);
            // assign a compass to the hunter
            TrackingCompassUtils.assignTrackingCompass(hunter);
            if(hunter.isTrackingPortal() || hunter.isTrackingDeath()){
                hunter.getEntity().setCompassTarget(hunter.getLastTracked());
            }
            hunter.updateCompassMeta();
        }
        // if respawning player is runner, notify the hunter
        if(TargetManager.hasRole(player,RoleEnum.RUNNER)){
            Target runner = TargetManager.getRunner(player);
            for(Hunter hunter : runner.getHunters()){
                hunter.setTrackingDeath(false);
                hunter.getEntity().sendMessage(player.getDisplayName() + " has respawned.");
                hunter.updateCompassMeta();
            }
        }
    }

}
