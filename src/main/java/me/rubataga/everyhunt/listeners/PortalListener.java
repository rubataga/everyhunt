package me.rubataga.manhunt.listeners;

import me.rubataga.manhunt.roles.Hunter;
import me.rubataga.manhunt.roles.RoleEnum;
import me.rubataga.manhunt.roles.Target;
import me.rubataga.manhunt.services.TargetManager;
import me.rubataga.manhunt.utils.TrackingCompassUtils;

import org.bukkit.entity.Entity;
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
            Target target = TargetManager.getTargets().get(entity);
            target.updateLastLocation();
            for(Hunter hunter : TargetManager.getTargets().get(entity).getHunters()){
                hunter.setTrackingPortal();
                // hunter in overworld, target joins otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    hunter.getEntity().setCompassTarget(target.getLastLocationDimension(hunter.getEntity().getWorld().getEnvironment()));
                }
                // hunter in otherworld, target joins overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    hunter.setLastTracked(target.getLastLocationDimension(hunter.getEntity().getWorld().getEnvironment()));
                }
            }
        }
        if(TargetManager.hasRole(entity,RoleEnum.HUNTER)) {
            Hunter hunter = TargetManager.getHunters().get(entity);
            if(hunter.getTarget()!=null){
                hunter.setTrackingPortal();
                // hunter joins overworld, target in otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    hunter.getEntity().setCompassTarget(hunter.getEntity().getLocation());
                }

                // hunter joins otherworld, target in overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    hunter.setLastTracked(hunter.getEntity().getLocation());
                }
            }
        }
    }


}