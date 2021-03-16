package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.Location;
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
                hunter.getEntity().sendMessage(entity.getName() + " has died. Now tracking " + entity.getName() + "'s death location.");
                hunter.setTrackingDeath(true);
                if(!hunter.isTrackingPortal()){
                    hunter.setLastTracked(entity.getLocation());
                }
                hunter.updateCompassMeta();
            }
        }
    }

    @EventHandler
    public void onEnderDragonDeath(EnderDragonChangePhaseEvent e){
        Entity dragon = e.getEntity();
        if(e.getNewPhase()==EnderDragon.Phase.DYING){
            anypercentWon(dragonHunterMap.getOrDefault(dragon, null));
        }
        targetDeathHandler(dragon);
    }

    private static void anypercentWon(Target target){
        StringBuilder victoryTextBuilder = new StringBuilder("The Runners win!");
        if (target!=null){
            victoryTextBuilder.insert(0,target.getEntity().getName()).append(" has killed the Ender Dragon! ");
        }
        String victoryText = victoryTextBuilder.toString();
        for(Entity hunter : TargetManager.getHunters().keySet()){
            hunter.sendMessage(victoryText);
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
            Location loc = player.getLocation();
            hunter.setLodestoneTracking(loc);
            hunter.setTrackingPortal();
            TrackingCompassUtils.assignTrackingCompass(hunter);
            if(hunter.isTrackingPortal() || hunter.isTrackingDeath()){
                Debugger.send("compass target 1");
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
