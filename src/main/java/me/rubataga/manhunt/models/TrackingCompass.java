//package me.rubataga.manhunt.models;
//
//import org.bukkit.Material;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class TrackingCompass {
//
//    private Hunter owner = null;
//    private ItemStack compass;
//
//    public TrackingCompass(){
//        //super(Material.COMPASS);
//        ItemMeta meta = this.getItemMeta();
//        meta.setDisplayName("§cTracking Compass");
//        this.setItemMeta(meta);
//
//    }
//
//    public void setOwner(Hunter hunter){
//        this.owner = hunter;
//    }
//
//    public Hunter getOwner(){
//        return owner;
//    }
//
//    public void updateTarget(){
//        ItemMeta meta = this.getItemMeta();
//        meta.setDisplayName("§cTracking Compass - " + owner.getTargetEntity().getName());
//        this.setItemMeta(meta);
//        owner.getEntity().updateInventory();
//    }
//
//    public void updateNull(){
//        ItemMeta meta = this.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        // track hunter's bed
//        if(owner.getEntity().getBedSpawnLocation()!=null){
//            owner.getEntity().setCompassTarget(owner.getEntity().getBedSpawnLocation());
//            compassLore.add("Target: bed spawn.");
//            // if no bed, track world spawn
//        } else {
//            owner.getEntity().setCompassTarget(owner.getEntity().getWorld().getSpawnLocation());
//            compassLore.add("Target: world spawn.");
//        }
//        meta.setLore(compassLore);
//        this.setItemMeta(meta);
//    }
//
//    public void updateDeath(){
//        ItemMeta meta = this.getItemMeta();
//        meta.setDisplayName("§cTracking Compass - " + owner.getTargetEntity().getName() + "'s death location");
//        meta.setLore(null);
//        this.setItemMeta(meta);
//    }
//
//    public void updatePortal(){
//        ItemMeta meta = this.getItemMeta();
//        List<String> compassLore = new LinkedList<>();
//        meta.setDisplayName("§cTracking Compass");
//        compassLore.add("Target is in a different world");
//        compassLore.add("Target: " + owner.getTargetEntity().getName() + "'s last world location.");
//        meta.setLore(compassLore);
//        this.setItemMeta(meta);
//    }
//
//}
