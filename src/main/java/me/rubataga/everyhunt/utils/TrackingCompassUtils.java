package me.rubataga.everyhunt.utils;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.roles.Hunter;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Class containing methods for {@link TrackingCompassUtils#trackingCompass(Hunter)}
 */
public class TrackingCompassUtils {

    private TrackingCompassUtils(){}

    private static final Everyhunt plugin = Everyhunt.getInstance();
    private static final NamespacedKey COMPASS_TIME_KEY = new NamespacedKey(plugin, "compassTime");
    private static final NamespacedKey TRACKING_COMPASS_KEY = new NamespacedKey(plugin, "trackingCompass");
    private static final TrackingCompassTagType TRACKING_COMPASS_TAG_TYPE = new TrackingCompassTagType();

    /**
     * Returns a vanilla compass named "Tracking Compass"
     *
     * @return a compass with a display name "Tracking Compass"
     */
    public static ItemStack vanillaTrackingCompass(Hunter hunter){
        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        trackingCompass.addUnsafeEnchantment(Enchantment.LUCK,1);
        CompassMeta meta = (CompassMeta)trackingCompass.getItemMeta();
        meta.setDisplayName("§cTracking Compass");
        //meta.setLodestoneTracked(false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        trackingCompass.setItemMeta(meta);
        return trackingCompass;
    }

    public static ItemStack vanillaTrackingCompass(){
        return vanillaTrackingCompass(null);
    }

    /**
     * Returns a {@link TrackingCompassUtils#vanillaTrackingCompass()} with an NBT boolean tag "isTrackingCompass" as true
     *
     * @return a compass with display name "Tracking Compass" and an NBT boolean tag "isTrackingCompass" = true
     */
    public static ItemStack trackingCompass(Hunter hunter){
        ItemStack tc = vanillaTrackingCompass(hunter);
        ItemMeta meta = tc.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(COMPASS_TIME_KEY, PersistentDataType.LONG,System.currentTimeMillis());
        if(hunter!=null) {
            pdc.set(TRACKING_COMPASS_KEY, TRACKING_COMPASS_TAG_TYPE, hunter);
        }
        tc.setItemMeta(meta);
        return tc;
    }

    public static ItemStack trackingCompass(){
        return trackingCompass(null);
    }

    public static ItemStack lodestoneTrackingCompass(Hunter hunter){
        ItemStack lc = trackingCompass(hunter);
        CompassMeta meta = (CompassMeta)lc.getItemMeta();
        meta.setLodestoneTracked(false);
        lc.setItemMeta(meta);
        return lc;
    }

    /**
     * Checks if an ItemStack is a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param compass ItemStack to check
     * @return true if item contains an NBT tag "isTrackingCompass" that is true
     */
    public static boolean isTrackingCompass(ItemStack compass){
        if(compass == null){
            return false;
        }
        ItemMeta meta = compass.getItemMeta();
        if(meta == null){
            return false;
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(compass.getType()==Material.COMPASS){
            return pdc.has(TRACKING_COMPASS_KEY, new TrackingCompassTagType());
        }
        return false;
    }
    /**
     * Returns a player's {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param inventory inventory to check for tracking compass
     * @return trackingCompass if player has one, null if player has 0 or more than 1 trackingCompasses
     */
    public static ItemStack getTrackingCompass(Inventory inventory){
        ItemStack trackingCompass = null;
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack item = inventory.getItem(i);
            if(item!=null){
                if(isTrackingCompass(item)){
                    if(trackingCompass==null){
                        trackingCompass=item;
                        //index=i;
                    } else {
                        inventory.setItem(i,null);
                    }
                }
            }
        }
        return trackingCompass;
    }

    public static void removeTrackingCompasses(Inventory inventory){
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack item = inventory.getItem(i);
            if(item!=null){
                if(isTrackingCompass(item)){
                    inventory.setItem(i,null);
                }
            }
        }
    }

    public static int getTrackingCompassIndex(Inventory inventory){
        int index = -1;
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack item = inventory.getItem(i);
            if(item!=null){
                if(isTrackingCompass(item)){
                    if(index==-1) {
                        index = i;
                    } else {
                        inventory.setItem(i,null);
                    }
                }
            }
        }
        return index;
    }


    public static boolean hasTrackingCompass(Hunter hunter){
        return hasTrackingCompass(hunter.getEntity());
    }

    public static boolean hasTrackingCompass(Player player){
        return hasTrackingCompass(player.getInventory());
    }

    public static boolean hasTrackingCompass(Inventory inventory){
        return getTrackingCompass(inventory)!=null;
    }

    public static boolean assignTrackingCompass(Hunter hunter){
        boolean hasCompass = true;
        ItemStack tc = getTrackingCompass(hunter.getEntity().getInventory());
        //hunter already has tracking compass
        if(tc==null) {
            hasCompass = false;
            tc = trackingCompass(hunter);
        }
        compassUpdater(hunter,tc);
        hunter.setCompass(tc,!hasCompass);
        return hasCompass;
    }

    public static void compassUpdater(Hunter hunter, ItemStack compass){
        StringBuilder displayName = new StringBuilder("§cTracking Compass");
        Player player = hunter.getEntity();
        // if hunter has no target
        if(hunter.getTarget()==null){
            if(player.getBedSpawnLocation()!=null){
                displayName.append(" - Bed Spawn");
                player.setCompassTarget(player.getBedSpawnLocation());
            } else {
                displayName.append(" - World Spawn");
                player.setCompassTarget(player.getWorld().getSpawnLocation());
            }
        // if hunter has target
        } else {
            displayName.append(" - ").append(hunter.getTarget());
            if (hunter.isTrackingDeath()) {
                displayName.append("'s death location");
            }
        }
        if (hunter.isTrackingPortal()) {
            displayName.append("'s portal");
        }
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(displayName.toString());
        compass.setItemMeta(meta);
        if(TrackingCompassUtils.hasTrackingCompass(player)){
            getTrackingCompass(player.getInventory()).setItemMeta(meta);
        }
    }
}
