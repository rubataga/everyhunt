package me.rubataga.manhunt.models;

import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompassRepeatingTask {

    public static Runnable compassRepeatingTask = ()-> {
        for (Map.Entry<Player, Entity> playerEntityEntry : TargetManager.getTargets().entrySet()) {
            Player hunter = (Player) ((Map.Entry) playerEntityEntry).getKey();
            Entity target = (Entity) ((Map.Entry) playerEntityEntry).getValue();
            if (hunter != null) {
                ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
                if(trackingCompass==null){
                    break;
                }
                ItemMeta meta = trackingCompass.getItemMeta();
                List<String> compassLore = new LinkedList<>();
                if (TargetManager.isTrackingDeath(hunter)) {
                    meta.setDisplayName("§cTracking Compass - " + target.getName() + "'s death location");
                } else if (target == null) {
                    meta.setDisplayName("§cTracking Compass");
                    try{
                        hunter.setCompassTarget(hunter.getBedSpawnLocation());
                        compassLore.add("Target: bed spawn.");
                    } catch (Exception e){
                        hunter.setCompassTarget(hunter.getWorld().getSpawnLocation());
                        compassLore.add("Target: world spawn.");
                    }
                } else {
                    meta.setDisplayName("§cTracking Compass - " + target.getName());
                    compassLore.add("Target: " + target.getName());
                    if (target.getWorld().equals(hunter.getWorld())) {
                        hunter.setCompassTarget(target.getLocation());
                    } else {
                        compassLore.add("Target is in a different world");
                        compassLore.add("Target: last world location.");

                    }
                }

                meta.setLore(compassLore);
                trackingCompass.setItemMeta(meta);
            }
        }
    };

}
