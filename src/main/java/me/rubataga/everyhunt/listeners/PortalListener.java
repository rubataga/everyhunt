package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerPortalEvent;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class PortalListener implements Listener {

    @EventHandler
    public void onEnterPortal(EntityPortalEnterEvent e){
        portal(e.getEntity());
    }

    @EventHandler
    public void onExitPortal(EntityPortalExitEvent e){
        portal(e.getEntity());
    }

    @EventHandler
    public void onPlayerEnterPortal(PlayerPortalEvent e){
        portal(e.getPlayer());
    }

    private void portal(Entity entity){
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) { // if teleporting entity is target
            Target target = TargetManager.getTarget(entity);
            target.updateLastLocation();
            for(Hunter hunter : target.getHunters()){
                Player player = hunter.getEntity();
                World.Environment environment = player.getWorld().getEnvironment();
                hunter.setTrackingPortal();
                // hunter in overworld, target joins otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    player.setCompassTarget(target.getLastLocationDimension(environment));
                }
                // hunter in otherworld, target joins overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    hunter.setLastTracked(target.getLastLocationDimension(environment));
                }
            }
        }
        if(TargetManager.hasRole(entity,RoleEnum.HUNTER)) {
            Hunter hunter = TargetManager.getHunter(entity);
            if(hunter.getTarget()!=null){
                Player player = hunter.getEntity();
                hunter.setTrackingPortal();
                // hunter joins overworld, target in otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    player.setCompassTarget(player.getLocation());
                }

                // hunter joins otherworld, target in overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    hunter.setLastTracked(player.getLocation());
                }
            }
        }
    }


}