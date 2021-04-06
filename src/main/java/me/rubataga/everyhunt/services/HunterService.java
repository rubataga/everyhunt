package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.exceptions.PlayerHasTrackingCompassException;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.utils.Rules;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.CommandSenderMessenger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HunterService {

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

    // split
    public static void track(CommandSender sender, Collection<Entity> targetsInit){
        Entity targetEntity = null;
        Target target;
        Player player = (Player) sender;
        Hunter hunter = TrackingManager.getHunter(player);
        if(hunter==null){
            player.sendMessage("You are not a hunter!");
            return;
        }
        //delete yourself from entityList
        targetsInit.remove(player);
        if(targetsInit.size()>0){
            if(targetsInit.size()==1){
               targetEntity = targetsInit.toArray(new Entity[1])[0];
                target = TrackingManager.getTarget(targetEntity);
                if(target!=null){
                   World targetWorld;
                   if(targetEntity.isDead()){
                       targetWorld = target.getDeathWorld();
                   } else {
                       targetWorld = targetEntity.getWorld();
                   }
                   Location targetHunterWorldLocation = target.getLastLocationWorld(player.getWorld());
                   if(targetWorld!=player.getWorld()){
                       if(targetHunterWorldLocation!=null){
                           hunter.setLastTracked(targetHunterWorldLocation);
                           hunter.setLodestoneTracking();
                           player.sendMessage("Now tracking " + targetEntity.getName() + "'s portal.");
                       } else {
                           player.sendMessage(targetEntity.getName() + " has not been in this world yet.");
                           return;
                       }
                   } else {
                       if(targetEntity.isDead()){
                           hunter.setLastTracked(targetHunterWorldLocation);
                           hunter.setTrackingDeath(true);
                           player.sendMessage("Now tracking " + targetEntity.getName() + "'s death location.");
                       }
                   }

               } else if (targetEntity.isDead() || targetEntity.getWorld()!=player.getWorld()){
                   player.sendMessage("You are not allowed to track " + targetEntity.getName());
                   return;
               } else {
                   target = TrackingService.addTarget(targetEntity);
                   player.sendMessage("Now tracking " + targetEntity.getName());
               }
               hunter.setTarget(target);
               hunter.updateCompassMeta();
               return;
            }
            player.sendMessage("Multiple targets found! Tracking closest target.");
            Double min = null;
            //find closest target
            for(Entity entity : targetsInit){
                //if entity is a player and the player isn't a runner, continue
                if(entity instanceof Player && !TrackingManager.getRunners().containsKey(entity)
                        || !(entity instanceof LivingEntity)
                        || player.getWorld() != entity.getWorld()
                        || Rules.isBlacklisted(entity)){
                    continue;
                }
                double dist = player.getLocation().distanceSquared(entity.getLocation());
                if(targetEntity==null || dist<min){
                    targetEntity = entity;
                    min = dist;
                }
            }
        }
        if(targetEntity!=null){
            target = TrackingService.addTarget(targetEntity);
            hunter.setTarget(target);
            player.sendMessage("Now tracking " + targetEntity.getName());
            hunter.updateCompassMeta();
        } else {
            player.sendMessage("No targets found!");
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