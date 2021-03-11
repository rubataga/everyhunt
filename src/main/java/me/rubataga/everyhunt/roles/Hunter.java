package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.guis.HunterGui;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class Hunter extends EveryhuntEntity{

    private Target target;
    private boolean lockedOnTarget;

    private ItemStack compass;
    private final ItemStack lodestoneCompass;
    private Location lastTracked;
    private final HunterGui gui;

    private boolean trackingDeath;
    private boolean trackingPortal;
    private boolean lodestoneTracking;

    public Hunter(Player player){
        super(player);
        this.gui = new HunterGui(this);
        this.lodestoneCompass = TrackingCompassUtils.lodestoneTrackingCompass(this);
    }

    public ItemStack getCompass(){
        return compass;
    }

    public void setCompass(ItemStack trackingCompass){
        setCompass(trackingCompass,false,-1);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv){
        setCompass(trackingCompass, addToInv, -1);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv, int slot){
        Inventory inventory = getEntity().getInventory();
        if(slot==-1){
            if(addToInv){
                slot = inventory.firstEmpty();
            } else {
                slot = TrackingCompassUtils.getTrackingCompassIndex(getEntity());
            }
        }
        if(isLodestoneTracking()){
            inventory.setItem(slot, lodestoneCompass);
            this.compass = inventory.getItem(slot);
        } else {
            if (addToInv) {
                inventory.setItem(slot, trackingCompass);
                this.compass = inventory.getItem(slot);
            } else {
                this.compass = trackingCompass;
            }
        }
        updateCompassMeta();
    }

    public ItemStack getLodestoneCompass(){
        return lodestoneCompass;
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

    public boolean isLockedOnTarget() {
        return lockedOnTarget;
    }

    public void setLockedOnTarget(boolean lockedOnTarget){
        this.lockedOnTarget = lockedOnTarget;
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

    public void setLodestoneTracking(Location finalLoc) {
        this.lodestoneTracking = finalLoc.getWorld().getEnvironment() != World.Environment.NORMAL;
        // if player is in overworld, un"lode" compass
        if(!lodestoneTracking && inventoryHasCompass()){
            CompassMeta meta = (CompassMeta)(compass.getItemMeta());
            meta.setLodestone(null);
            compass.setItemMeta(meta);
        }
    }

    public void setLodestoneTracking() {
        setLodestoneTracking(getEntity().getLocation());
    }

    public void setTrackingPortal(World hunterWorld, World targetWorld){
        this.trackingPortal = hunterWorld != targetWorld;
    }

    public void setTrackingPortal(Location hunterLoc, Location targetLoc){
        setTrackingPortal(hunterLoc.getWorld(),targetLoc.getWorld());
    }

    public void setTrackingPortal(){
        World checkWorld;
        if(trackingDeath){
            checkWorld = lastTracked.getWorld();
        } else if (target==null) {
            Location bed = getEntity().getBedSpawnLocation();
            if (bed == null) {
                checkWorld = getEntity().getWorld();
            } else {
                checkWorld = bed.getWorld();
            }
        } else {
            checkWorld = getTargetEntity().getLocation().getWorld();
        }
        setTrackingPortal(getEntity().getLocation().getWorld(),checkWorld);
    }


    public boolean isLodestoneTracking(){
        return lodestoneTracking;
    }

    public void setTarget(Target target){
        if(this.target!=null){
            this.target.removeHunter(this);
        }
        this.target = target;
        if(target!=null){
            target.addHunter(this);
            this.trackingDeath = target.getEntity().isDead();
            setLodestoneTracking();
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

}
