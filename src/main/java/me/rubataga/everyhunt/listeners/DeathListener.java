package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.events.RunnerDeathByHunterEvent;
import me.rubataga.everyhunt.events.RunnerDeathEvent;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;

public class DeathListener implements Listener {

    private static final PluginManager PM = Bukkit.getPluginManager();

    /**
     * EventHandler for a target dying
     *
     * @param e {@link EntityDeathEvent}
     */
    @EventHandler
    public void onTargetDeath(EntityDeathEvent e){
        targetDeathHandler(e.getEntity());
    }

    @EventHandler
    public void onTargetExplode(EntityExplodeEvent e){
        targetDeathHandler(e.getEntity());
    }

    @EventHandler
    public void onTargetDamage(EntityDamageByEntityEvent e){
        Entity targetEntity = e.getEntity();
        Target runnerTarget = TrackingManager.getTarget(targetEntity);
        if(runnerTarget!=null) {
            if(targetEntity.isDead()){
                Entity damagerEntity = e.getDamager();
                Hunter damagerHunter = TrackingManager.getHunter(damagerEntity);
                if(damagerHunter!=null){
                    PM.callEvent(new RunnerDeathByHunterEvent(runnerTarget,damagerHunter));
                } else {
                    PM.callEvent(new RunnerDeathEvent(runnerTarget));
                }
            }
        }
    }

    private void targetDeathHandler(Entity targetEntity){
        Target target = TrackingManager.getTarget(targetEntity);
        if(target!=null) {
            target.updateLastLocation();
            target.updateDeathWorld();
            for (Hunter hunter : target.getHunters()) {
                hunter.getEntity().sendMessage(targetEntity.getName() + " has died. Now tracking " + targetEntity.getName() + "'s death location.");
                hunter.setTrackingDeath(true);
                if(!hunter.isTrackingPortal()){
                    hunter.setLastTracked(targetEntity.getLocation());
                }
                hunter.updateCompassMeta();
            }
            EntityDamageEvent damageEvent = targetEntity.getLastDamageCause();
            targetDeathEventCaller(damageEvent,target);
        }
    }

    private void targetDeathEventCaller(EntityDamageEvent e, Target target){
        Debugger.send("A target has died!");
        Entity targetEntity = target.getEntity();
        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) e;
            if (TrackingManager.hasRole(targetEntity, RoleEnum.RUNNER)) {
                Entity damagerEntity = damageByEntityEvent.getDamager();
                Hunter damagerHunter = TrackingManager.getHunter(damagerEntity);
                if (damagerHunter != null) {
                    PM.callEvent(new RunnerDeathByHunterEvent(target, damagerHunter));
                    return;
                }
            }
        }
        PM.callEvent(new RunnerDeathEvent(target));
    }

    /**
     * EventHandler for a hunter or runner respawning
     *
     * @param e {@link PlayerRespawnEvent}
     */
    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e){
        // if respawning player is hunter, give trackingCompass
        Player player = e.getPlayer();
        if(TrackingManager.hasRole(player, RoleEnum.HUNTER)){
            // initialize the hunter object
            Hunter hunter = TrackingManager.getHunter(player);
            // assign a compass to the hunter
            Location loc = player.getLocation();
            hunter.setLodestoneTracking(loc);
            hunter.setTrackingPortal();
            TrackingCompassUtils.assignTrackingCompass(hunter);
            if(hunter.isTrackingPortal() || hunter.isTrackingDeath()){
                hunter.getEntity().setCompassTarget(hunter.getLastTracked());
            }
            hunter.updateCompassMeta();
        }
        // if respawning player is runner, notify the hunter
        if(TrackingManager.hasRole(player,RoleEnum.RUNNER)){
            Target runner = TrackingManager.getRunner(player);
            for(Hunter hunter : runner.getHunters()){
                hunter.setTrackingDeath(false);
                hunter.getEntity().sendMessage(player.getDisplayName() + " has respawned.");
                hunter.updateCompassMeta();
            }
        }
    }

}
