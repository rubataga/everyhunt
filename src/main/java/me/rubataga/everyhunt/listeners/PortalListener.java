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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

/**
 * Listener for events relating to {@link TrackingCompassUtils}
 */
public class PortalListener implements Listener {

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent e){
        teleportHandler(e.getEntity(),e.getFrom(),e.getTo());
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e){
        teleportHandler(e.getEntity(), e.getFrom(),e.getTo());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        teleportHandler(e.getPlayer(),e.getFrom(),e.getTo());
    }

    private void teleportHandler(Entity entity, Location from, Location to){
        World toWorld = to.getWorld();
        World fromWorld = from.getWorld();
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) {
            Target target = TargetManager.getTarget(entity);
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
        if(TargetManager.hasRole(entity,RoleEnum.HUNTER)) {
            Hunter hunter = TargetManager.getHunter(entity);
            Target target = hunter.getTarget();
            boolean initialLodestoneTrackingStatus = hunter.isLodestoneTracking();

            hunter.setLodestoneTracking(to);
            hunter.setTrackingPortal(to);
            Debugger.send("hunter lodestone tracking? " + hunter.isLodestoneTracking());
            Debugger.send("hunter tracking portal? " + hunter.isTrackingPortal());
            // if target has a lastLocation in the toWorld, use it
            Location targetLast = target.getLastLocationWorld(toWorld);
            if(toWorld!=fromWorld) {
                hunter.setLastTracked(Objects.requireNonNullElse(targetLast, to));
            }
            if(initialLodestoneTrackingStatus!=hunter.isLodestoneTracking()){
                hunter.updateCompassLodedStatus();
            }
            hunter.updateCompassMeta();
        }
    }


}