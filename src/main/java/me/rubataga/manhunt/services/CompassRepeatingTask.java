package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.Hunter;
import me.rubataga.manhunt.utils.TrackingCompassUtils;

/**
 * Class containing the task that updates a {@link TrackingCompassUtils#trackingCompass()}
 */
public class CompassRepeatingTask {

    private CompassRepeatingTask(){}

    /**
     * Task that updates the compass location of a hunter with a {@link TrackingCompassUtils#trackingCompass()}
     */
    public static Runnable compassRepeatingTask = ()-> {
        //for each hunter with a target
        for (Hunter hunter : TargetManager.getHunters().values()) {
            if (hunter != null && hunter.getTarget() != null) {
                // if hunter is tracking death or portal, don't set compass. compass will continue to track the target's last location.
                if(hunter.getCompass() == null || hunter.isTrackingDeath() || hunter.isTrackingPortal()){
                    continue;
                }
                hunter.getEntity().setCompassTarget(hunter.getTargetEntity().getLocation());
            }
        }
    };
}
