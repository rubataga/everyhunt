package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class TeleportListener implements Listener {

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e){
        Location to = e.getTo();
        if(to==null){
            to = e.getFrom();
        }
        teleportHandler(e.getEntity(),e.getFrom(),to);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e){
        Location to = e.getTo();
        if(to==null){
            to = e.getFrom();
        }
        teleportHandler(e.getEntity(), e.getFrom(),to);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        Location to = e.getTo();
        if(to==null){
            to = e.getFrom();
        }
        teleportHandler(e.getPlayer(),e.getFrom(),to);
    }

    private void teleportHandler(Entity entity, Location from, Location to){
        World toWorld = to.getWorld();
        World fromWorld = from.getWorld();
        if(TrackingManager.hasRole(entity,RoleEnum.TARGET)) {
            Target target = TrackingManager.getTarget(entity);
            target.updateWorldLocation(toWorld, to);
            target.updateWorldLocation(fromWorld, from);
            target.updateLastLocation();
            if(toWorld!=fromWorld){
                for(Hunter hunter : target.getHunters()){
                    Player player = hunter.getEntity();
                    World world = player.getWorld();
                    hunter.setLodestoneTracking(from);
                    hunter.setTrackingPortal(player.getLocation(),to);
                    hunter.setLastTracked(target.getLastLocationWorld(world));
                    hunter.updateCompassMeta();
                }
            }
        }
        if(TrackingManager.hasRole(entity,RoleEnum.HUNTER)) {
            Hunter hunter = TrackingManager.getHunter(entity);
            Target target = hunter.getTarget();
            boolean initialLodestoneTrackingStatus = hunter.isLodestoneTracking();

            hunter.setLodestoneTracking(to);
            hunter.setTrackingPortal(to);
            Debugger.send("hunter lodestone tracking? " + hunter.isLodestoneTracking());
            Debugger.send("hunter tracking portal? " + hunter.isTrackingPortal());
            // if target has a lastLocation in the toWorld, use it
            Location targetLast = null;
            if(target!=null){
                targetLast = target.getLastLocationWorld(toWorld);
            }
            if (target==null || targetLast==null){
                targetLast = to;
            }
            if(toWorld!=fromWorld) {
                hunter.setLastTracked(targetLast);
            }
            if(initialLodestoneTrackingStatus!=hunter.isLodestoneTracking()){
                Debugger.send("updating compass status!");
                hunter.updateCompassLodedStatus();
            }
            hunter.updateCompassMeta();
        }
    }


}