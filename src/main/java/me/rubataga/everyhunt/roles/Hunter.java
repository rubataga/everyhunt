package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.guis.HunterGui;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class Hunter extends EveryhuntRole {

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

    public void setCompass(){
        setCompass(TrackingCompassUtils.trackingCompass(this));
    }

    public void setCompass(ItemStack trackingCompass){
        setCompass(trackingCompass,false);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv){
        setCompass(trackingCompass, addToInv, -1);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv, int slot){
        setCompass(trackingCompass,addToInv,slot,false);
    }

    public void setCompass(ItemStack trackingCompass, boolean addToInv, int slot, boolean replace){
        Inventory inventory = getEntity().getInventory();
        // slot == -1 indicates a nonexistent inventory slot
        if(slot==-1){
            if(replace){
                slot = TrackingCompassUtils.getTrackingCompassIndex(getEntity().getInventory());
            } else if(addToInv){
                slot = inventory.firstEmpty();
            } else {
                slot = TrackingCompassUtils.getTrackingCompassIndex(getEntity().getInventory());
            }
            if(slot==-1){
                slot = inventory.firstEmpty();
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

    public void updateCompassLodedStatus(){
        if(lodestoneTracking){
            Debugger.send("updating loded status, lodestone tracking");
            setCompass(lodestoneCompass,true,-1,true);
        } else {
            Debugger.send("updating loded status, not lodestone tracking");
            setCompass(TrackingCompassUtils.trackingCompass(this),true,-1,true);
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

    public void setTrackingPortal(boolean trackingPortal){
        this.trackingPortal = trackingPortal;
    }

    public void setTrackingPortal(World hunterWorld, World targetWorld){
        this.trackingPortal = hunterWorld != targetWorld;
    }

    public void setTrackingPortal(Location hunterLoc, Location targetLoc){
        if(hunterLoc!=null && targetLoc!=null){
            setTrackingPortal(hunterLoc.getWorld(),targetLoc.getWorld());
        } else {
            setTrackingPortal();
        }
    }

    public void setTrackingPortal(Location hunterLoc){
        World checkWorld;
        World hunterWorld = hunterLoc.getWorld();
        if(trackingDeath){
            checkWorld = target.getDeathWorld();
        } else if (target==null) {
            Location bed = getEntity().getBedSpawnLocation();
            if (bed != null) {
                checkWorld = bed.getWorld();
            } else {
                setTrackingPortal(hunterWorld.getEnvironment() != World.Environment.NORMAL);
                return;
            }
        } else {
            checkWorld = target.getEntity().getWorld();
        }
        setTrackingPortal(hunterWorld,checkWorld);
    }

    public void setTrackingPortal(){
        setTrackingPortal(getEntity().getLocation());
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

    public void track(Target target){
        if(!GameCfg.huntersCanChangeTarget){ // this is a little goofy
            getEntity().sendMessage("You aren't allowed to change your target!");
        } else {
            setTarget(target);
            if(target==null){
                getEntity().sendMessage("Tracker reset!");
            } else {
                getEntity().sendMessage("Now tracking " + getTargetEntity().getName() + ".");
            }
            updateCompassMeta();
        }
    }

    public Target getTarget(){
        return this.target;
    }

    public Entity getTargetEntity(){
        if(target==null){
            return null;
        }
        return target.getEntity();
    }

    public void updateCompassMeta(){
        TrackingCompassUtils.compassUpdater(this, compass);
        gui.draw();
    }

}
