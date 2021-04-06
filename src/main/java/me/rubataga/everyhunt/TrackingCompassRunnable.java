package me.rubataga.everyhunt;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class TrackingCompassRunnable {

    /**
     * Task that updates the compass location of a hunter with a {@link TrackingCompassUtils#trackingCompass()}
     */
    public static final Runnable compassRepeatingTask = ()-> {
        //for each hunter with a target
        for (Hunter hunter : TrackingManager.getHunters().values()) {
            if(hunter.getCompass() == null){
                continue;
            }
            Target target = hunter.getTarget();
            if(!hunter.isLodestoneTracking()) {
                Location playerCompassLocation = hunter.getEntity().getCompassTarget();
                if (hunter.isTrackingDeath() || hunter.isTrackingPortal()){
                    if (playerCompassLocation == hunter.getLastTracked()){
                        continue;
                    }
                    playerCompassLocation = hunter.getLastTracked();
                } else if (target!=null){
                    playerCompassLocation = target.getEntity().getLocation();
                } else {
                    playerCompassLocation = hunter.getLastTracked();
                }
                if(playerCompassLocation==null){
                    playerCompassLocation = hunter.getEntity().getWorld().getSpawnLocation();
                }
                hunter.getEntity().setCompassTarget(playerCompassLocation);
            // if lodestone tracking
            } else {
                ItemStack compass;
                if (TrackingCompassUtils.hasTrackingCompass(hunter)) {
                    compass = hunter.getCompass();
                } else {
                    compass = hunter.getLodestoneCompass();
                }
                CompassMeta meta = ((CompassMeta) (compass.getItemMeta()));
                meta.setLodestoneTracked(false);
                Location lodeLocation = meta.getLodestone();
                // if tracking death/portal, the target location shouldn't be changing
                if (hunter.isTrackingDeath() || hunter.isTrackingPortal()) {
                    // lodeLocation should equal the hunter's lastTracked. else, update it
                    if (lodeLocation == hunter.getLastTracked()) {
                        continue;
                    }
                    lodeLocation = hunter.getLastTracked();
                } else if (target != null) {
                    Debugger.send("tracking new entity location!");
                    lodeLocation = target.getEntity().getLocation();
                } else {
                    lodeLocation = hunter.getLastTracked();
                }
                meta.setLodestone(lodeLocation);
                compass.setItemMeta(meta);
                hunter.getGUI().draw();
            }
        }
    };

}
