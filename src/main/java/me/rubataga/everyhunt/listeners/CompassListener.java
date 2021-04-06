package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.utils.Rules;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class CompassListener implements Listener {

    private static final List<InventoryAction> illegalTrackingCompassActions = new ArrayList<>();

    static{
        illegalTrackingCompassActions.add(InventoryAction.PLACE_ONE);
        illegalTrackingCompassActions.add(InventoryAction.PLACE_SOME);
        illegalTrackingCompassActions.add(InventoryAction.PLACE_ALL);
    }

    /**
     * EventHandler for a hunter right-clicking while holding a {@link TrackingCompassUtils#trackingCompass(Hunter)}
     *
     * @param e {@link PlayerInteractEvent}
     */
    @EventHandler
    public void onHunterUseTrackingCompass(PlayerInteractEvent e){
        Action action = e.getAction();
        if(action==Action.LEFT_CLICK_AIR || action==Action.LEFT_CLICK_BLOCK || action==Action.PHYSICAL){
            return;
        }
        Player player = e.getPlayer();
        if(e.getHand().equals(EquipmentSlot.OFF_HAND) || //if listening for OFF_HAND
                !TrackingManager.hasRole(player, RoleEnum.HUNTER) || // if the event player is not a hunter
                !(TrackingCompassUtils.isTrackingCompass(e.getItem()))){// if the event player is not holding a Tracking Compass
            return;
        }
        Hunter hunter = TrackingManager.getHunter(player);
        if(player.isSneaking()){
            if(GameCfg.guiEnabled){
                hunter.getGUI().show();
            }
            return;
        }
        if(hunter.isLockedOnTarget()){
            return;
        }
        e.setCancelled(true);
        Target target = hunter.getTarget();
        // if hunter is tracking dead runner, queue hunter to track runner when they revive
        if(hunter.isTrackingDeath()){
            if(target!=null) {
                if (TrackingManager.getRunnerList().contains(target)){
                    player.sendMessage("Tracking " + target.getEntity().getName() + "'s death location until they revive.");
                    return;
                }
            }
            if(GameCfg.rightClickResetsDeadCompass){
                hunter.setTrackingDeath(false);
                hunter.track(null);
                return;
            }
        }
        if(!GameCfg.rightClickCyclesRunners){
            return;
        }
        if(TrackingManager.getRunners().size()==0) { // if no runners
            return;
        }
        if(target!=null){
            // if not tracking player
            if(!(target.getEntity() instanceof Player)){
                return;
            }
        }
        int runnerIndex = 0;
        List<Target> runnerList = TrackingManager.getRunnerList();
        // if there's only one runner
        if(runnerList.size()==1){
            // if the single runner is also the hunter
            if(runnerList.get(0).getEntity()==player){
                return;
            }
            // if there are multiple runners, cycle to and select the next runner
        } else {
            if(target==null){
                target = TrackingManager.getRunnerList().get(0);
            }
            Entity targetEntity = target.getEntity();
            if(TrackingManager.hasRole(targetEntity,RoleEnum.RUNNER)){
                runnerIndex = runnerList.indexOf(TrackingManager.getRunner(targetEntity)) + 1; // cycle to the next runner in the ArrayList
            }
            while(runnerList.get(runnerIndex).getEntity()==player){
                runnerIndex++;
                if(runnerIndex >= runnerList.size()){
                    runnerIndex = 0;
                }
            }
        }
        Target runner = runnerList.get(runnerIndex);
        hunter.track(runner);
    }

    /**
     * EventHandler for a hunter right-clicking an entity while holding a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @param e {@link PlayerInteractEntityEvent}
     */
    @EventHandler
    public void onHunterInteractWithEntity(PlayerInteractEntityEvent e){
        // if event is firing for offhand or player isn't holding tracking compass
        if(e.getHand().equals(EquipmentSlot.OFF_HAND)){
            return;
        }
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        // if not holding tracking compass or not clicking LivingEntity
        if((!TrackingCompassUtils.isTrackingCompass(player.getInventory().getItemInMainHand())) ||
                !(entity instanceof LivingEntity)){
            return;
        }
        Hunter hunter = TrackingManager.getHunter(player);
        if(hunter==null){
            return;
        }
        e.setCancelled(true);
        if(!TrackingManager.hasRole(player,RoleEnum.HUNTER)){
            player.sendMessage("You are not a hunter!");
            return;
        }
        // set the player's target to the clicked entity
        if(player.isSneaking()){
            return;
        }
        if(hunter.isLockedOnTarget()){
            e.setCancelled(false);
            return;
        }
        // if hunter is already tracking the clicked entity
        if(hunter.getTargetEntity()==entity){
            return;
        }
        if(Rules.isBlacklisted(entity)){
            player.sendMessage(GameCfg.blacklistMessage);
            return;
        }
        Target target;
        // if clicked entity is a target, set target to the clicked entity
        target = TrackingManager.getTarget(entity);
        if(target==null){
            target = TrackingService.addTarget(entity);
        }
        hunter.track(target);
    }

    @EventHandler
    public void onTrackingCompassPickup(EntityPickupItemEvent e){
        // if item isn't TrackingCompass, return
        Item item = e.getItem();
        if(!TrackingCompassUtils.isTrackingCompass(item.getItemStack())){
            return;
        }
        Entity entity = e.getEntity();
        // no one should ever pick up a tracking compass, so cancel
        e.setCancelled(true);
        // if not a hunter, return
        if(!TrackingManager.hasRole(entity, RoleEnum.HUNTER)){
            return;
        }
        Hunter hunter = TrackingManager.getHunter(entity);
        // only hunters with no tracking compass in their inventory can pick one up
        if(!TrackingCompassUtils.hasTrackingCompass(hunter)) {
            item.remove();
            TrackingCompassUtils.assignTrackingCompass(hunter);
            hunter.updateCompassMeta();
        }
    }

    // when player gets into a bed and their target is null, update their compass
    @EventHandler
    public void onRespawnSetBed(PlayerBedLeaveEvent e){
        Player player = e.getPlayer();
        if(TrackingManager.hasRole(player,RoleEnum.HUNTER)){
            Hunter hunter = TrackingManager.getHunter(player);
            if(hunter.getTarget()==null){
                hunter.updateCompassMeta();
            }
        }
    }

    @EventHandler
    public void onInventoryCompassDrag(InventoryDragEvent e) {
        if(TrackingCompassUtils.isTrackingCompass(e.getOldCursor())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryCompassClick(InventoryClickEvent e) {
        InventoryAction action = e.getAction();
        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if(TrackingCompassUtils.isTrackingCompass(currentItem)){
            if (e.isRightClick()){// && compassPickupActions.contains(action)) {
                Hunter hunter = TrackingManager.getHunter(player);
                if (hunter != null) {
                    if(GameCfg.guiEnabled){
                        hunter.getGUI().show();
                    }
                    e.setCancelled(true);
                }
            }
        }
        // if cursor has tc, cancel if trying to put it in a player's inventory who already
        // has a tc, or cancel if trying to stack
        boolean cursorIsCompass = TrackingCompassUtils.isTrackingCompass(cursorItem);
        boolean currentIsCompass = TrackingCompassUtils.isTrackingCompass(currentItem);
        if(cursorIsCompass || currentIsCompass) {
            Inventory inventory = e.getClickedInventory();
            if(inventory==null){
                return;
            }
            if(TrackingCompassUtils.hasTrackingCompass(player)){
                if(inventory instanceof PlayerInventory){
                    if(cursorIsCompass){
                        if(illegalTrackingCompassActions.contains(action)) {
                            e.setCancelled(true);
                        }
                    }
                } else {
                    if(currentIsCompass){
                        if(action==InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                            e.setCancelled(true);
                        }
                    }
                }
            }

        }
    }
}
