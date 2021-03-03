package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.game.GameCfg;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CompassService {

    public static void giveCompass(CommandSender sender, Collection<Entity> hunters){
        for(Entity entity : hunters){
            Player player;
            if(entity instanceof Player){
                player = (Player) entity;
            } else {
                continue;
            }
            boolean senderIsPlayer = sender == player;
            if(!TargetManager.hasRole(player, RoleEnum.HUNTER)){
                if(senderIsPlayer){
                    sender.sendMessage("You are not a hunter!");
                } else {
                    sender.sendMessage(player.getName() + " is not a hunter!");
                }
            } else {
                Hunter hunter = TargetManager.getHunter(player);
                // if hunter has a tracking compass in their inventory
                if(TrackingCompassUtils.assignTrackingCompass(hunter)){
                    if (senderIsPlayer) {
                        sender.sendMessage("You already have a Tracking Compass!");
                    } else {
                        sender.sendMessage(player.getName() + " already has a Tracking Compass!");
                    }
                } else {
                    if (!senderIsPlayer) {
                        sender.sendMessage(player.getName() + " was given a Tracking Compass!");
                    }
                    hunter.updateCompassMeta();
                }
            }
        }
    }

    public static void track(CommandSender sender, Collection<Entity> targetsInit){
        Entity targetEntity = null;
        Target target;
        Player player = (Player) sender;
        Hunter hunter;
        if(!TargetManager.hasRole(player,RoleEnum.HUNTER)){
            player.sendMessage("You are not a hunter!");
            return;
        }
        hunter = TargetManager.getHunter(player);
        //delete yourself from entityList
        targetsInit.remove(player);
        if(targetsInit.size()>0){
            if(targetsInit.size()==1){
               targetEntity = targetsInit.toArray(new Entity[1])[0];
               if(TargetManager.hasRole(targetEntity,RoleEnum.TARGET)){
                   target = TargetManager.getTarget(targetEntity);
                   World targetWorld;
                   if(targetEntity.isDead()){
                       targetWorld = target.getDeathWorld();
                   } else {
                       targetWorld = targetEntity.getWorld();
                   }
                   Location targetHunterDimensionLocation = target.getLastLocationDimension(player.getWorld().getEnvironment());
                   if(targetWorld!=player.getWorld()){
                       if(targetHunterDimensionLocation!=null){
                           hunter.setLastTracked(targetHunterDimensionLocation);
                           hunter.setTrackingPortal();
                           player.sendMessage("Now tracking " + targetEntity.getName() + "'s portal.");
                       } else {
                           player.sendMessage("Sorry, " + targetEntity.getName() + " has not been in this world yet.");
                           return;
                       }
                   } else {
                       if(targetEntity.isDead()){
                           hunter.setLastTracked(targetHunterDimensionLocation);
                           hunter.setTrackingDeath(true);
                           player.sendMessage("Now tracking " + targetEntity.getName() + "'s death location.");
                       }
                   }

               } else if (targetEntity.isDead() || targetEntity.getWorld()!=player.getWorld()){
                   player.sendMessage("Sorry, you are not allowed to track " + targetEntity.getName());
                   return;
               } else {
                   target = new Target(targetEntity);
                   TargetManager.addTarget(target);
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
                if(entity instanceof Player && !TargetManager.getRunners().containsKey(entity)
                        || !(entity instanceof LivingEntity)
                        || player.getWorld() != entity.getWorld()
                        || GameCfg.isBlacklisted(entity)){
                    continue;
                }
                double dist = player.getLocation().distanceSquared(entity.getLocation());
                if(targetEntity==null){
                    targetEntity = entity;
                    min = dist;
                }
                else if(dist<min){
                    targetEntity=entity;
                    min=dist;
                }
            }
        }
        if(targetEntity!=null){
            if(TargetManager.hasRole(targetEntity,RoleEnum.TARGET)){
                target = TargetManager.getTargets().get(targetEntity);
            } else {
                target = new Target(targetEntity);
                TargetManager.addTarget(target);
            }
            hunter.setTarget(target);
            player.sendMessage("Now tracking " + targetEntity.getName());
            hunter.updateCompassMeta();

        } else {
            player.sendMessage("No targets found!");
        }
    }

    public static void reset(CommandSender sender){
        Player player = (Player) sender;
        Hunter hunter;
        if(!TargetManager.hasRole(player,RoleEnum.HUNTER)){
            player.sendMessage("You are not a hunter!");
            return;
        }
        hunter = TargetManager.getHunter(player);
        hunter.setTarget(null);
        hunter.setLastTracked(null);
        hunter.updateCompassMeta();
        player.sendMessage("Compass reset!");
    }
}