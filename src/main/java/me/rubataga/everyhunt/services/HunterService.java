package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.events.HunterTrackTargetEvent;
import me.rubataga.everyhunt.exceptions.PlayerHasTrackingCompassException;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.utils.Rules;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.CommandSenderMessenger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;

public class HunterService {

    private static final PluginManager PM = Bukkit.getPluginManager();

    public static void giveCompassCommand(CommandSender sender, Collection<Entity> hunters) {
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        for(Entity entity : hunters){
            Player player = (Player) entity;
            try{
                giveCompass(player);
                csm.setYouString("You were");
                csm.setOtherString(" was");
                csm.povMessage(player," given a Tracking Compass!",sender!=player,false);
            } catch (EntityHasRoleException e){
                csm.povMessageSenderOnly(player," not a hunter!");
            } catch (PlayerHasTrackingCompassException e){
                csm.setYouString("You already have");
                csm.setOtherString(" already has");
                csm.povMessage(player," a Tracking Compass!");
            }
        }
    }

    public static void giveCompass(Player player) throws EntityHasRoleException, PlayerHasTrackingCompassException {
        //boolean senderIsPlayer = sender == player;
        Hunter hunter = TrackingManager.getHunter(player);
        if(hunter==null) {
            throw new EntityHasRoleException(player, RoleEnum.HUNTER, false);
        }
        hunter = TrackingManager.getHunter(player);
            // if hunter has a tracking compass in their inventory
        if(TrackingCompassUtils.assignTrackingCompass(hunter)){
            throw new PlayerHasTrackingCompassException(player);
        } else {
            hunter.updateCompassMeta();
        }
    }

    public static void track(Hunter hunter, Target target){
            hunter.setTarget(target);
            if(target==null){
                hunter.getEntity().sendMessage("Tracker reset!");
            } else {
                hunter.getEntity().sendMessage(getHunterTrackingMessage(hunter));
            }
            hunter.updateCompassMeta();
            PM.callEvent(new HunterTrackTargetEvent(hunter,target));
    }

    public static String getHunterTrackingMessage(Hunter hunter){
        StringBuilder sb = new StringBuilder("Now tracking " + hunter.getTarget());
        if(hunter.isTrackingDeath()){
            sb.append("'s death location");
        }
        if(hunter.isTrackingPortal()){
            sb.append("'s portal");
        }
        sb.append(".");
        return sb.toString();
    }

    private static void trackCommandForSingleTarget(Hunter hunter, Entity targetEntity){
        Player hunterPlayer = hunter.getEntity();
        Target target = TrackingManager.getTarget(targetEntity);
        if(target!=null){
            World targetWorld;
            World hunterWorld = hunterPlayer.getWorld();
            if(targetEntity.isDead()){
                targetWorld = target.getDeathWorld();
            } else {
                targetWorld = targetEntity.getWorld();
            }
            // get the target's last location in the hunter's current world
            Location targetLastHunterWorldLocation = target.getLastWorldLocation(hunterWorld);
            // hunter and target are in different worlds.
            if(targetWorld!=hunterWorld){
                // if target has been in this world before, hunter will track their last world location (probably a portal)
                if(targetLastHunterWorldLocation!=null){
                    hunter.setLastTracked(targetLastHunterWorldLocation);
                    hunter.setLodestoneTracking();
                } else {
                    hunterPlayer.sendMessage(targetEntity.getName() + " has not been in this world yet.");
                    return;
                }
            } else {
                if(targetEntity.isDead() && GameCfg.huntersCanTrackDeadTargets){
                    hunter.setLastTracked(targetLastHunterWorldLocation);
                    hunter.setTrackingDeath(true);
                    hunterPlayer.sendMessage(getHunterTrackingMessage(hunter));
                }
            }
        }
        // the inputted targetEntity has no associated Target
        else if (targetEntity.isDead() || targetEntity.getWorld()!=hunterPlayer.getWorld()){
            hunterPlayer.sendMessage("You are not allowed to track " + targetEntity.getName());
            return;
        } else {
            target = TrackingService.addTarget(targetEntity);
        }
        hunter.track(target);
    }

    // split
    public static void trackCommand(CommandSender sender, Collection<Entity> targetsInit){
        Entity targetEntity = null;
        Target target;
        Player hunterPlayer = (Player) sender;
        Hunter hunter = TrackingManager.getHunter(hunterPlayer);
        if(hunter==null){
            sender.sendMessage("You are not a hunter!");
            return;
        }
        if(!GameCfg.huntersCanChangeTarget){
            sender.sendMessage("You are not allowed to change your target!");
        }
        //delete yourself from entityList
        targetsInit.remove(hunterPlayer);
        if(targetsInit.size()>0){
            if(targetsInit.size()==1){
                targetEntity = targetsInit.toArray(new Entity[1])[0];
//                target = TrackingManager.getTarget(targetEntity);
                trackCommandForSingleTarget(hunter,targetEntity);
                return;
            }
            hunterPlayer.sendMessage("Multiple targets found! Tracking closest target.");
            Double min = null;
            //find closest target
            for(Entity entity : targetsInit){
                //if entity is a player and the player isn't a runner, continue
                if(entity instanceof Player && !TrackingManager.getRunners().containsKey(entity)
                        || !(entity instanceof LivingEntity)
                        || hunterPlayer.getWorld() != entity.getWorld()
                        || Rules.isBlacklisted(entity)){
                    continue;
                }
                double dist = hunterPlayer.getLocation().distanceSquared(entity.getLocation());
                if(targetEntity==null || dist<min){
                    targetEntity = entity;
                    min = dist;
                }
            }
        }
        if(targetEntity!=null){
            target = TrackingService.addTarget(targetEntity);
            hunter.track(target);
        } else {
            hunterPlayer.sendMessage("No targets found!");
        }
    }

    public static void reset(CommandSender sender){
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        Player player = (Player) sender;
        Hunter hunter;
        if(!TrackingManager.hasRole(player,RoleEnum.HUNTER)){
            csm.message("You are not a hunter!");
            return;
        }
        hunter = TrackingManager.getHunter(player);
        hunter.setTarget(null);
        hunter.setLastTracked(null);
        hunter.setTrackingDeath(false);
        hunter.setLodestoneTracking();
        hunter.setTrackingPortal();
        hunter.updateCompassMeta();
        csm.message("Compass reset!");
    }

    public static void gui(CommandSender sender){
        Hunter hunter = TrackingManager.getHunter((Entity)sender);
        if(hunter!=null){
            hunter.getGUI().show();
        } else {
            sender.sendMessage("Only hunters can do this!");
        }
    }
}