package me.rubataga.manhunt.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class TrackingCompass {

    private TrackingCompass(){}

    public static ItemStack getTrackingCompass(){
        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackingCompass.getItemMeta();

        meta.setDisplayName("Tracking Compass");
        trackingCompass.setItemMeta(meta);

        return trackingCompass;
    }

}
