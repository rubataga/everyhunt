package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.GameRules;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

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
            if(targetsInit.size()>1){
                player.sendMessage("Multiple targets found! Tracking closest target.");
            }
            Double min = null;
            //find closest target
            for(Entity entity : targetsInit){
                //if entity is a player and the player isn't a runner, continue
                if(entity instanceof Player && !TargetManager.getRunners().containsKey(entity)
                        || !(entity instanceof LivingEntity)
                        || player.getWorld() != entity.getWorld()
                        || GameRules.isBlacklisted(entity)){
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

    public static void recalibrate(CommandSender sender){
        Player player = (Player) sender;
        Hunter hunter;
        if(!TargetManager.hasRole(player,RoleEnum.HUNTER)){
            player.sendMessage("You are not a hunter!");
            return;
        }
        hunter = TargetManager.getHunter(player);
        hunter.setTarget(null);
        hunter.updateCompassMeta();
        player.sendMessage("Compass reset!");
    }
}