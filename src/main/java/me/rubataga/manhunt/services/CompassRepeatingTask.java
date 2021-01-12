package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class containing the task that updates a {@link TrackingCompassUtils#trackingCompass()}
 */

public class CompassRepeatingTask {

    private CompassRepeatingTask(){}

    /**
     * Task that updates the compass location of a hunter with a {@link TrackingCompassUtils#trackingCompass()}
     */

    public static Runnable compassRepeatingTask = ()-> {
        // for each entrySet of Player, Entity in hunterTargetMap
        for (Map.Entry<Player, Entity> playerEntityEntry : TargetManager.getHunterTargetMap().entrySet()) {
            Player hunter = playerEntityEntry.getKey();
            Entity target = playerEntityEntry.getValue();
            // if the hunter exists
            if (hunter != null) {
                // set trackingCompass to the hunter's trackingCompass
                ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
                if(trackingCompass==null){
                    break;
                }
                ItemMeta meta = trackingCompass.getItemMeta();
                List<String> compassLore = new LinkedList<>();
                // if the hunter is tracking a death location
                if (TargetManager.isTrackingDeath(hunter)) {
                    meta.setDisplayName("§cTracking Compass - " + target.getName() + "'s death location");
                // if hunter has no target
                } else if (target == null) {
                    meta.setDisplayName("§cTracking Compass");
                    // track hunter's bed
                    if(hunter.getBedSpawnLocation()!=null){
                        hunter.setCompassTarget(hunter.getBedSpawnLocation());
                        compassLore.add("Target: bed spawn.");
                    // if no bed, track world spawn
                    } else {
                        hunter.setCompassTarget(hunter.getWorld().getSpawnLocation());
                        compassLore.add("Target: world spawn.");
                    }
                // if hunter has a target and isn't tracking a death location
                } else {
                    meta.setDisplayName("§cTracking Compass - " + target.getName());
                    // if target is in the same world world
                    if (target.getWorld().equals(hunter.getWorld())) {
                        compassLore.add("Target: " + target.getName());
                        hunter.setCompassTarget(target.getLocation());
                    } else {
                        compassLore.add("Target is in a different world");
                        compassLore.add("Target: " + target.getName() + "'s last world location.");
                    }
                }
                // update the trackingCompass' lore and meta
                meta.setLore(compassLore);
                trackingCompass.setItemMeta(meta);
            }
        }
    };

}
