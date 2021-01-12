package me.rubataga.manhunt.models;

import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.tr7zw.changeme.nbtapi.NBTItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing the constructor for a {@link TrackingCompassUtils#trackingCompass()}
 */

public class TrackingCompassUtils {

    private TrackingCompassUtils(){}

    /**
     * Returns a vanilla compass named "Tracking Compass"
     *
     * @return a compass with a display name "Tracking Compass"
     */

    public static ItemStack vanillaTrackingCompass(){
        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackingCompass.getItemMeta();

        meta.setDisplayName("§cTracking Compass");
        trackingCompass.setItemMeta(meta);

        return trackingCompass;
    }

    /**
     * Returns a {@link TrackingCompassUtils#vanillaTrackingCompass()} with an NBT boolean tag "isTrackingCompass" as true
     *
     * @return a compass with display name "Tracking Compass" and an NBT boolean tag "isTrackingCompass" = true
     */


    public static ItemStack trackingCompass(){
        NBTItem trackingCompass = new NBTItem(vanillaTrackingCompass());
        trackingCompass.setBoolean("isTrackingCompass",true);
        return trackingCompass.getItem();
    }

    /**
     * Gives a player a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param player player to give tracking comapss
     * @return true if player is a hunter
     */

    public static boolean giveTrackingCompass(Player player){
        if(TargetManager.isHunter(player)){
            player.getInventory().addItem(trackingCompass());
            return true;
        }
        return false;
    }

    /**
     * Returns a player's {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param player player to check for tracking compass
     * @return trackingCompass if player has one, null if player has 0 or more than 1 trackingCompasses
     */

    public static ItemStack getTrackingCompass(Player player){
        PlayerInventory inventory = player.getInventory();
        //ItemStack[] trackingCompass = Arrays.stream(inventory.getContents()).filter(item -> item.getType() == Material.COMPASS && item.getItemMeta().getDisplayName().contains("Tracking Compass")).toArray(ItemStack[]::new);
        List<ItemStack> trackingCompasses = new ArrayList<>();
        for(ItemStack item : inventory.getContents()){
            if(item!=null) {
                if(isTrackingCompass(item)){
                    trackingCompasses.add(item);
                }
            }
        }
        if(trackingCompasses.size()!=1){
            return null;
        }
        return trackingCompasses.get(0);
    }

    /**
     * Checks if an ItemStack is a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param compass ItemStack to check
     * @return true if item contains an NBT tag "isTrackingCompass" that is true
     */

    public static boolean isTrackingCompass(ItemStack compass){
        // AP CSA: Comparing objects with ==
        if(compass.getType()==Material.COMPASS){
            NBTItem trackingCompass = new NBTItem(compass);
            return trackingCompass.getBoolean("isTrackingCompass");
        }
        return false;
    }

//    @Deprecated
//    public static boolean giveCompass(Player hunter){
//        if(TargetManager.isHunter(hunter)){
//            if(hunter.getInventory().contains(Material.COMPASS)){
//                List<ItemStack> compasses = Arrays.stream(hunter.getInventory().getContents()).filter(p -> p.getType()==Material.COMPASS).collect(Collectors.toList());
//                for(ItemStack compass : compasses){
//                    if(isTrackingCompass(compass)){
//                        return false;
//                    }
//                }
//            }
//            hunter.getInventory().addItem(TrackingCompassUtils.vanillaTrackingCompass());
//            return true;
//        }
//        return false;
//    }

//    @Deprecated
//    public static boolean hasTrackingCompass(Player player){
//        PlayerInventory inventory = player.getInventory();
//        if(inventory.contains(Material.COMPASS)){
//            ItemStack[] compasses = Arrays.stream(inventory.getContents()).filter(item -> item.getType() == Material.COMPASS).toArray(ItemStack[]::new);
//            for (ItemStack compass : compasses) {
//                if(compass.getItemMeta().getDisplayName().contains("Tracking Compass")){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

}
