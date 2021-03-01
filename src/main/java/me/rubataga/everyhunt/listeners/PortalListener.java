package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class PortalListener implements Listener {

//    @EventHandler
//    public void onEnterPortal(EntityPortalEnterEvent e){
//        portal(e.getEntity());
//    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e){
        portal(e.getEntity(),e.getFrom(), e);
    }

    @EventHandler
    public void onPlayerEnterPortal(PlayerTeleportEvent e){
        portal(e.getPlayer(),e.getFrom(), e);
    }

//    @EventHandler
//    public void onEntityTeleport(EntityTeleportEvent e){
//    }

    private void portal(Entity entity, Location from, Event e){
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) {
            Target target = TargetManager.getTarget(entity);
            target.updateDimensionLocation(from.getWorld().getEnvironment(), from);
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
                    if(e instanceof PlayerPortalEvent){
                        hunter.setLastTracked(player.getLocation());
                    } else {
                        Target target = hunter.getTarget();
                        Location lastDimensionLocation = target.getLastLocationDimension(player.getWorld().getEnvironment());
                        if(lastDimensionLocation!=null){
                            hunter.setLastTracked(lastDimensionLocation);
                        } else {
                            hunter.setLastTracked(player.getLocation());
                            player.sendMessage("No location information found for " + target.getEntity().getName());
                        }
                    }
                }

                // hunter joins overworld, target dead in overworld
                if(!hunter.isTrackingPortal() && !hunter.isLodestoneTracking() && hunter.isTrackingDeath()){
                    player.setCompassTarget(hunter.getTarget().getLastLocationDimension(player.getWorld().getEnvironment()));
                }
            }
        }
    }


}