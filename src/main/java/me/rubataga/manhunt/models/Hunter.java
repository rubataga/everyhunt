package me.rubataga.manhunt.models;

import me.rubataga.manhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hunter extends GameEntity{

    //private UUID targetID;
    private Target target;
    private boolean trackingDeath = false;
    private boolean trackingPortal = false;
    private ItemStack compass;

    public Hunter(Player player){
        super(player);
    }

    public ItemStack getCompass(){
        return compass;
    }

    public void setCompass(ItemStack trackingCompass){
        int slot = getEntity().getInventory().firstEmpty();
        //System.out.println("BUGCHECK: setCompass: slot - " + slot + ", compass name: " + trackingCompass.getItemMeta().getDisplayName());
        getEntity().getInventory().setItem(slot,trackingCompass.clone());
        this.compass = getEntity().getInventory().getItem(slot);
    }

    public Player getEntity(){
        return (Player) super.getEntity();
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
        return this.target.getEntity();
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
