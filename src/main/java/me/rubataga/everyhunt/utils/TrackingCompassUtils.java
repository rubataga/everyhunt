package me.rubataga.everyhunt.utils;

import me.rubataga.everyhunt.roles.Hunter;
import de.tr7zw.changeme.nbtapi.NBTItem;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Class containing methods for {@link TrackingCompassUtils#trackingCompass()}
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
        trackingCompass.addUnsafeEnchantment(Enchantment.LUCK,1);
        CompassMeta meta = (CompassMeta)trackingCompass.getItemMeta();
        meta.setDisplayName("§cTracking Compass");
        meta.setLodestoneTracked(false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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

    public static ItemStack lodestoneTrackingCompass(){
        ItemStack lc = trackingCompass();
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
        if(compass.getType()==Material.COMPASS){
            NBTItem trackingCompass = new NBTItem(compass);
            return trackingCompass.getBoolean("isTrackingCompass");
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
        ItemStack trackingCompass = null;
        //int index = 0;
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack item = inventory.getItem(i);
            if(item!=null){
                if(isTrackingCompass(item)){
                    if(trackingCompass==null){
                        //System.out.println("compass found!");
                        trackingCompass=item;
                        //index=i;
                    } else {
                        //System.out.println("removed extra compass");
                        inventory.setItem(i,null);
                    }
                }
            }
        }
        return trackingCompass;
    }

    public static ItemStack getTrackingCompass(Hunter hunter){
        return getTrackingCompass(hunter.getEntity());
    }

    public static int getTrackingCompassIndex(Player player){
        PlayerInventory inventory = player.getInventory();
        boolean searching = true;
        int index = 0;
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack item = inventory.getItem(i);
            if(item!=null){
                if(isTrackingCompass(item)){
                    if(searching) {
                        //System.out.println("compass found!");
                        searching = false;
                        index = i;
                    }
//                    } else {
//                        System.out.println("removed extra compass");
//                        inventory.setItem(i,null);
//                    }
                }
            }
        }
        return index;
    }

    public static boolean hasTrackingCompass(Player player){
        return getTrackingCompass(player)!=null;
    }

    public static boolean hasTrackingCompass(Hunter hunter){
        return hasTrackingCompass(hunter.getEntity());
    }

    public static boolean assignTrackingCompass(Hunter hunter){
        boolean compassFound = true;
        ItemStack tc = getTrackingCompass(hunter.getEntity());
        //hunter already has tracking compass
        if(tc==null) {

            compassFound = false;
            tc = trackingCompass();
        }
        compassUpdater(hunter,tc);
        hunter.setCompass(tc,!compassFound);
        //System.out.println("tc null: " + (tc==null));
        return compassFound;
    }
    // if inventory has no compass
    //if(!hunter.inventoryHasCompass()){
//            System.out.println("§bdoesn't have compass!");
//            // if hunter has assigned compass, give them that compass
//            if(hunter.getCompass()!=null){
//                System.out.println("§badding assigned");
//                ItemStack tempCompass = hunter.getCompass();
//                System.out.println("temp compass null?" + (tempCompass==null));
//                hunter.setCompass(tempCompass,true);
//                // else give hunter new compass
//            } else {
//                System.out.println("§bmaking new compass!");
    //hunter.setCompass(trackingCompass(),true);
//            }
    // if hunter has compass in inventory, set their compass to this compass
    //} else {
//            System.out.println("§bhas compass!");
    //hunter.setCompass(trackingCompass(),false);
    //hunter.setCompass(TrackingCompassUtils.getTrackingCompass(hunter.getEntity()),false);
    //}
//        System.out.println("compass null?" + (hunter.getCompass()==null));


    public static void compassUpdater(Hunter hunter, ItemStack compass){
        //System.out.println("§bupdating compass for " + hunter.getEntity().getName());
        StringBuilder displayName = new StringBuilder("§cTracking Compass");
        // if hunter has no target
        if(hunter.getTarget()==null){
            //System.out.println("§cBUGCHECK1");
            if(hunter.getEntity().getBedSpawnLocation()!=null){
                //System.out.println("§cBUGCHECK2");
                displayName.append(" - Bed Spawn");
                hunter.getEntity().setCompassTarget(hunter.getEntity().getBedSpawnLocation());
            } else {
                //System.out.println("§cBUGCHECK3");
                displayName.append(" - World Spawn");
                hunter.getEntity().setCompassTarget(hunter.getEntity().getWorld().getSpawnLocation());
            }
        // if hunter has target
        } else {
            //System.out.println("§cBUGCHECK4");
            displayName.append(" - ").append(hunter.getTargetEntity().getName());
            if (hunter.isTrackingDeath()) {
                //System.out.println("§cBUGCHECK5");
                displayName.append("'s death location");
            } else if (hunter.isTrackingPortal()) {
                //System.out.println("§cBUGCHECK6");
                displayName.append("'s portal");
            }
        }
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(displayName.toString());
        compass.setItemMeta(meta);
        if(TrackingCompassUtils.hasTrackingCompass(hunter.getEntity())){
            //System.out.println("§cBUGCHECK7");
            getTrackingCompass(hunter.getEntity()).setItemMeta(meta);
        }
        //hunter.getEntity().updateInventory();

        //System.out.println("§bBUGCHECK: hunter's updated compass displayname: " + hunter.getCompass().getItemMeta().getDisplayName());
        //System.out.println("§bBUGCHECK: meta displayname: " + meta.getDisplayName());
        //System.println("§bBUGCHECK: UtilGetCompass displayname: " + getTrackingCompass(hunter.getEntity()).getItemMeta().getDisplayName());
    }

//
//    public static void compassUpdateTrackingDeath(Player hunter){
//        TargetManagerDep.runTargetUpdate(hunter);
//        Entity target = TargetManagerDep.getHunterTarget(hunter);
//        ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
//        ItemMeta meta = trackingCompass.getItemMeta();
//        meta.setDisplayName("§cTracking Compass - " + target.getName() + "'s death location");
//        meta.setLore(null);
//        trackingCompass.setItemMeta(meta);
//    }
//
//    public static void compassUpdateTrackingNull(Player hunter){
//        TargetManagerDep.removeHunterTrackingPortal(hunter);
//        TargetManagerDep.removeHunterTrackingDeath(hunter);
//        ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
//        ItemMeta meta = trackingCompass.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        // track hunter's bed
//        if(hunter.getBedSpawnLocation()!=null){
//            hunter.setCompassTarget(hunter.getBedSpawnLocation());
//            compassLore.add("Target: bed spawn.");
//            // if no bed, track world spawn
//        } else {
//            hunter.setCompassTarget(hunter.getWorld().getSpawnLocation());
//            compassLore.add("Target: world spawn.");
//        }
//        meta.setLore(compassLore);
//        trackingCompass.setItemMeta(meta);
//    }
//
//    public static void compassUpdateTrackingPortal(Player hunter){
//        TargetManagerDep.runTargetUpdate(hunter);
//        Entity target = TargetManagerDep.getHunterTarget(hunter);
//        ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
//        ItemMeta meta = trackingCompass.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        compassLore.add("Target is in a different world");
//        compassLore.add("Target: " + target.getName() + "'s last world location.");
//        meta.setLore(compassLore);
//        trackingCompass.setItemMeta(meta);
//    }
//
//    public static void compassUpdateTarget(Player hunter){
//        Entity target = TargetManagerDep.getHunterTarget(hunter);
//        ItemStack trackingCompass = TrackingCompassUtils.getTrackingCompass(hunter);
//        ItemMeta meta = trackingCompass.getItemMeta();
//        meta.setDisplayName("§cTracking Compass - " + target.getName());
//        trackingCompass.setItemMeta(meta);
//    }
//
//    public static void runCompassUpdate(Hunter hunter) {
//        if(TargetManagerDep.getHunterTarget(hunter)==null){
//            TrackingCompassUtils.compassUpdateTrackingNull(hunter);
//        } else if(TargetManagerDep.isTrackingDeath(hunter)){
//            TrackingCompassUtils.compassUpdateTrackingDeath(hunter);
//        } else if(TargetManagerDep.isTrackingPortal(hunter)){
//            TrackingCompassUtils.compassUpdateTrackingPortal(hunter);
//        } else {
//            TrackingCompassUtils.compassUpdateTarget(hunter);
//        }
//    }
    //
//
//    /**
//     * Gives a player a {@link TrackingCompassUtils#trackingCompass()}
//     *
//     * @param hunter player to give tracking compass
//     * @return true if player is a hunter
//     */
////    public static void giveTrackingCompass(Hunter hunter){
////        hunter.setCompass(new TrackingCompass());
////    }
}
