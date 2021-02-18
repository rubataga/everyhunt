package me.rubataga.manhunt.models;

import me.rubataga.manhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hunter extends ManhuntEntity {

    private Target target;
    private ItemStack compass;
    private Location lastTracked;

    private boolean trackingDeath = false;
    private boolean trackingPortal = false;
    private boolean locked = false;
    private final HunterGui gui = new HunterGui(this);

    public Hunter(Player player){
        super(player);
    }

    public ItemStack getCompass(){
        return compass;
    }

    // set the hunter's assigned compass, add it to inventory if addCompassToInventory is true
    public void setCompass(ItemStack trackingCompass, boolean addCompassToInventory){
        if(addCompassToInventory) {
            int slot = getEntity().getInventory().firstEmpty();
            getEntity().getInventory().setItem(slot, trackingCompass.clone());
            this.compass = getEntity().getInventory().getItem(slot);
        } else {
            this.compass = trackingCompass;
        }
    }

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

    public void setTrackingPortal(boolean trackingPortal) {
        this.trackingPortal = trackingPortal;
    }


    public void setTarget(Target target){
        if(this.target!=null){
            this.target.removeHunter(this);
        }
        if(target!=null){
            target.addHunter(this);
        }
        this.target = target;
        this.trackingDeath = false;
        this.trackingPortal = false;
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

    public void updateCompass(){
        TrackingCompassUtils.compassUpdater(this);
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
