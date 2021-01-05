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

public class TrackingCompassUtils {

    private TrackingCompassUtils(){}

    public static ItemStack vanillaTrackingCompass(){
        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackingCompass.getItemMeta();

        meta.setDisplayName("§cTracking Compass");
        trackingCompass.setItemMeta(meta);

        return trackingCompass;
    }

    public static ItemStack trackingCompass(){
        NBTItem trackingCompass = new NBTItem(vanillaTrackingCompass());
        trackingCompass.setBoolean("isTrackingCompass",true);
        return trackingCompass.getItem();
    }

    public static boolean giveTrackingCompass(Player player){
        if(TargetManager.isHunter(player)){
            player.getInventory().addItem(trackingCompass());
            return true;
        }
        return false;
    }

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

    public static boolean isTrackingCompass(ItemStack compass){
        if(compass.getType()==Material.COMPASS){
            NBTItem trackingCompass = new NBTItem(compass);
            return trackingCompass.getBoolean("isTrackingCompass");
        }
        return false;
    }
//
//    public static boolean giveCompass(Player hunter){
//        if(isHunter(hunter)){
//            if(hunter.getInventory().contains(Material.COMPASS)){
//                List<ItemStack> compasses = Arrays.stream(hunter.getInventory().getContents()).filter(p -> p.getType()==Material.COMPASS).collect(Collectors.toList());
//                for(ItemStack compass : compasses){
//                    if(isTrackingCompass(compass)){
//                        return false;
//                    }
//                }
//            }
//            hunter.getInventory().addItem(TrackingCompass.getTrackingCompass());
//            return true;
//        }
//        return false;
//    }

}
