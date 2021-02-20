package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.guis.HunterGui;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class Hunter extends EveryhuntEntity {

    private Target target;
    private ItemStack compass;
    private final ItemStack lodestoneCompass;
    private Location lastTracked;
    private final HunterGui gui;

    private boolean trackingDeath;
    private boolean trackingPortal;
    private boolean lodestoneTracking;
    private boolean locked;

    /*
    compasses
    compass variable refers to the only compass a player has
    -- upon receiving a new compass ino the inventory
        -- generate compass name from variables
        -- if lodestoneTracking, set compass as lodestone compass
     */

    public Hunter(Player player){
        super(player);
        this.gui = new HunterGui(this);
        this.lodestoneCompass = TrackingCompassUtils.lodestoneTrackingCompass();
    }

    public ItemStack getCompass(){
        return compass;
    }

    public void setCompass(ItemStack trackingCompass){
        setCompass(trackingCompass,false);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv){
        if(isLodestoneTracking()){
            int slot;
            if(addToInv){
                slot = getEntity().getInventory().firstEmpty();
            } else {
                slot = TrackingCompassUtils.getTrackingCompassIndex(getEntity());
            }
            getEntity().getInventory().setItem(slot, lodestoneCompass);
            this.compass = getEntity().getInventory().getItem(slot);
        } else {
            if (addToInv) {
                int slot = getEntity().getInventory().firstEmpty();
                getEntity().getInventory().setItem(slot, trackingCompass);
                this.compass = getEntity().getInventory().getItem(slot);
            } else {
                this.compass = trackingCompass;
            }
        }
        updateCompassMeta();
    }

    public ItemStack getLodestoneCompass(){
        return lodestoneCompass;
    }

//
//
//    public void setCompass(int slot){
//        if(lodestoneTracking){
//            this.compass = getEntity().getInventory().getItem(slot);
//        }
//    }
//    // set the hunter's assigned compass, add it to inventory if addCompassToInventory is true
//    public void setCompassComplicated(ItemStack trackingCompass){
//        if(lodestoneTracking){
//            TrackingCompassUtils.compassUpdater(this,trackingCompass);
//            int slot = getEntity().getInventory().firstEmpty();
//            getEntity().getInventory().setItem(slot, trackingCompass);
//            this.compass = getEntity().getInventory().getItem(slot);
//        }
//    }
//
//    public void setCompass(ItemStack trackingCompass, boolean addCompassToInventory){
//        if(addCompassToInventory) {
//            int slot = getEntity().getInventory().firstEmpty();
//            getEntity().getInventory().setItem(slot, trackingCompass.clone());
//            this.compass = getEntity().getInventory().getItem(slot);
//        } else {
//            this.compass = trackingCompass;
//        }
//    }

    public boolean inventoryHasCompass() {
        return TrackingCompassUtils.hasTrackingCompass(getEntity());
    }

    public Player getEntity(){
        return (Player) super.getEntity();
    }

    public void setLastTracked(Location location){
        this.lastTracked = location;
    }

    public Location getLastTracked(){
        return lastTracked;
    }

    public HunterGui getGUI() {
        return gui;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public boolean isTrackingDeath() {
        return trackingDeath;
    }

    public void setTrackingDeath(boolean trackingDeath) {
        this.trackingDeath = trackingDeath;
    }

    public boolean isTrackingPortal() {
        return trackingPortal;
    }

    public void setTrackingPortal() {
        this.lodestoneTracking = getEntity().getWorld().getEnvironment()!= World.Environment.NORMAL;
        this.trackingPortal = getEntity().getWorld()!=getTargetEntity().getWorld();
        // if player is in overworld, un"lode" compass
        if(!lodestoneTracking && inventoryHasCompass()){
            CompassMeta meta = (CompassMeta)(compass.getItemMeta());
            meta.setLodestone(null);
            compass.setItemMeta(meta);
        }
        updateCompassMeta();
    }

    public boolean isLodestoneTracking(){
        return lodestoneTracking;
    }

    public void setTarget(Target target){
        if(this.target!=null){
            //System.out.println("BUGCHECK removed this hunter from target");
            this.target.removeHunter(this);
        }
        this.target = target;
        if(target!=null){
            //System.out.println("BUGCHECK added this hunter to target");
            target.addHunter(this);
            this.trackingDeath = target.getEntity().isDead();
            setTrackingPortal();
        }
    }

    public Entity getTargetEntity(){
        if(target==null){
            return null;
        }
        return target.getEntity();
    }

    public Target getTarget(){
        return this.target;
    }

    public void updateCompassMeta(){
        TrackingCompassUtils.compassUpdater(this, compass);
        gui.draw();
    }


//    public void messagePlayer(String string){
//        getEntity().sendMessage(string);
//        System.out.println(string);
//    }

    //
//    public void compassUpdateTarget(){
//        messagePlayer("BUGCHECK Compass Updating");
//        ItemMeta meta = compass.getItemMeta();
//        if(meta!=null){
//            messagePlayer("BUGCHECK Metadata Found!");
//        }
//        meta.setDisplayName("§cTracking Compass - " + target.getEntity().getName());
//        compass.setItemMeta(meta);
//        player.updateInventory();
//        messagePlayer("BUGCHECK Display name: " + compass.getItemMeta().getDisplayName());
//    }
//
//    private void compassUpdateNull(){
//        ItemMeta meta = compass.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        // track hunter's bed
//        if(player.getBedSpawnLocation()!=null){
//            player.setCompassTarget(player.getBedSpawnLocation());
//            compassLore.add("Target: bed spawn.");
//            // if no bed, track world spawn
//        } else {
//            player.setCompassTarget(player.getWorld().getSpawnLocation());
//            compassLore.add("Target: world spawn.");
//        }
//        meta.setLore(compassLore);
//        compass.setItemMeta(meta);
//    }
//
//    public void compassUpdateDeath(){
//        ItemMeta meta = compass.getItemMeta();
//        meta.setDisplayName("§cTracking Compass - " + target.getEntity().getName() + "'s death location");
//        meta.setLore(null);
//        compass.setItemMeta(meta);
//    }
//
//    public void compassUpdatePortal(){
//        ItemMeta meta = compass.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        compassLore.add("Target is in a different world");
//        compassLore.add("Target: " + target.getEntity().getName() + "'s last world location.");
//        meta.setLore(compassLore);
//        compass.setItemMeta(meta);
//    }
}
