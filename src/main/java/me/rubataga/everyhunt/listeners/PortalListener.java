package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class PortalListener implements Listener {

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e){
        teleportHandler(e.getEntity(),e.getFrom(),e.getTo(), e);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e){
        teleportHandler(e.getEntity(), e.getFrom(),e.getTo(), e);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        teleportHandler(e.getPlayer(),e.getFrom(),e.getTo(), e);
    }

    private void teleportHandler(Entity entity, Location from, Location to, Event e){
        Debugger.send("Event name: " + e.getEventName());
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) {
            Target target = TargetManager.getTarget(entity);
            target.updateWorldLocation(from.getWorld(), from);
            target.updateWorldLocation(to.getWorld(), to);
            target.updateLastLocation();
            for(Hunter hunter : target.getHunters()){
                Player player = hunter.getEntity();
                World world = player.getWorld();
                hunter.setLodestoneTracking(from);
                hunter.setTrackingPortal(player.getLocation(),to);
                // hunter in overworld, target joins otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    player.setCompassTarget(target.getLastLocationWorld(world));
                }
                // hunter in otherworld, target joins overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    hunter.setLastTracked(target.getLastLocationWorld(world));
                }
                hunter.updateCompassMeta();
            }
        }
        if(TargetManager.hasRole(entity,RoleEnum.HUNTER)) {
            Hunter hunter = TargetManager.getHunter(entity);
            Target target = hunter.getTarget();
            if(target!=null){
                Player player = hunter.getEntity();
                hunter.setLodestoneTracking(to);
                hunter.setTrackingPortal(to,hunter.getTargetEntity().getLocation());
                // hunter joins overworld, target in otherworld
                if(hunter.isTrackingPortal() && !hunter.isLodestoneTracking()){
                    player.setCompassTarget(player.getLocation());
                }
                // hunter joins otherworld, target in overworld
                if(hunter.isTrackingPortal() && hunter.isLodestoneTracking()){
                    Debugger.send("Hunter is Portal Tracking and Lodestone Tracking");
                    if(to.getWorld()!=from.getWorld()){
                        //Location lastPlayerLocation = player.getLocation();
                        Debugger.send("Player last location world: " + to.getWorld().getName());
                        hunter.setLastTracked(to);
                    } else {
                        Debugger.send("Player is in same world");
                        Location lastWorldLocation = target.getLastLocationWorld(to.getWorld());
                        if(lastWorldLocation!=null){
                            hunter.setLastTracked(lastWorldLocation);
                        } else {
                            hunter.setLastTracked(player.getLocation());
                            player.sendMessage("No location information found for " + target.getEntity().getName());
                        }
                    }
                }

                // hunter joins overworld, target dead in overworld
                if(!hunter.isTrackingPortal() && !hunter.isLodestoneTracking() && hunter.isTrackingDeath()){
                    player.setCompassTarget(target.getLastLocationWorld(player.getWorld()));
                }
                hunter.updateCompassMeta();
            }
        }
    }


}