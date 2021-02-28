package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class CompassRunnable {

    /**
     * Task that updates the compass location of a hunter with a {@link TrackingCompassUtils#trackingCompass()}
     */
    public static final Runnable compassRepeatingTask = ()-> {
        //for each hunter with a target
        for (Hunter hunter : TargetManager.getHunters().values()) {
            Target target = hunter.getTarget();
            if (target != null) {
                //System.out.println(hunter.getEntity().getName() + "'s compass null: " + (hunter.getCompass()==null));
                // if hunter is tracking death or portal, don't set compass. compass will continue to track the target's last location.
                if(hunter.getCompass() == null || (!hunter.isLodestoneTracking() && (hunter.isTrackingDeath() || hunter.isTrackingPortal()))){
                    continue;
                }
                // lodestone tracking logic used when the hunter is in the nether or the end
                // lodestone tracking requires interval CompassMeta refreshes
                if(hunter.isLodestoneTracking()){
                    ItemStack compass;
                    if(TrackingCompassUtils.hasTrackingCompass(hunter)){
                        compass = hunter.getCompass();
                    } else {
                        compass = hunter.getLodestoneCompass();
                    }
//                    if(!hunter.inventoryHasCompass()){
//                        return;
//                    }
                    CompassMeta meta = ((CompassMeta)(compass.getItemMeta()));
                    meta.setLodestoneTracked(false);
                    // if tracking a portal or a death location (which don't change), track lastTracked
                    if (hunter.isTrackingPortal() || hunter.isTrackingDeath()) {
                        meta.setLodestone(hunter.getLastTracked());
                    } else {
                        meta.setLodestone(target.getEntity().getLocation());
                    }
                    compass.setItemMeta(meta);
                    hunter.getGUI().draw();
                } else {
                    hunter.getEntity().setCompassTarget(target.getEntity().getLocation());
                }
            }
        }
    };

}
