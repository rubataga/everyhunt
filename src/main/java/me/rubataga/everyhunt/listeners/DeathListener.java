package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.game.GameProgression;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

public class DeathListener implements Listener {

    private final Map<Entity,Target> dragonHunterMap = new HashMap<>();

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

    private void targetDeathHandler(Entity entity){
        if(TargetManager.hasRole(entity,RoleEnum.TARGET)) {
            Target target = TargetManager.getTarget(entity);
            target.updateLastLocation();
            target.updateDeathWorld();
            for (Hunter hunter : target.getHunters()) {
                hunter.setTrackingDeath(true);
                hunter.getEntity().sendMessage(entity.getName() + " has died. Now tracking " + hunter.getTargetEntity().getName() + "'s death location.");
                hunter.setLastTracked(entity.getLocation());
                hunter.updateCompassMeta();
            }
        }
    }

    @EventHandler
    public void onEnderDragonDeath(EnderDragonChangePhaseEvent e){
        Entity dragon = e.getEntity();
        if(e.getNewPhase()==EnderDragon.Phase.DYING){
            GameProgression.anypercentWon(dragonHunterMap.getOrDefault(dragon, null));
        }
    }

    @EventHandler
    public void onTargetKillEnderDragon(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        Entity dragon = e.getEntity();
        if(!TargetManager.hasRole(damager,RoleEnum.TARGET) || dragon.getType()!= EntityType.ENDER_DRAGON){
            return;
        }
        if(dragon.isDead()){
            dragonHunterMap.put(dragon,TargetManager.getTarget(damager));
        }
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
        if(TargetManager.hasRole(player, RoleEnum.HUNTER)){
            // initialize the hunter object
            Hunter hunter = TargetManager.getHunter(player);
            // assign a compass to the hunter
            TrackingCompassUtils.assignTrackingCompass(hunter);
            if(hunter.isTrackingPortal() || hunter.isTrackingDeath()){
                hunter.getEntity().setCompassTarget(hunter.getLastTracked());
            }
            hunter.updateCompassMeta();
        }
        // if respawning player is runner, notify the hunter
        if(TargetManager.hasRole(player,RoleEnum.RUNNER)){
            Target runner = TargetManager.getRunner(player);
            for(Hunter hunter : runner.getHunters()){
                hunter.setTrackingDeath(false);
                hunter.getEntity().sendMessage(player.getDisplayName() + " has respawned.");
                hunter.updateCompassMeta();
            }
        }
    }

}
