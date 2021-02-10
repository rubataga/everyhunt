package me.rubataga.manhunt.services;

import me.rubataga.manhunt.models.Hunter;
import me.rubataga.manhunt.models.RoleEnum;
import me.rubataga.manhunt.models.Target;
import me.rubataga.manhunt.utils.TrackingCompassUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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
            if(!TargetManager.hasRole(player.getUniqueId(), RoleEnum.HUNTER)){
                if(senderIsPlayer){
                    sender.sendMessage("You are not a hunter!");
                } else {
                    sender.sendMessage(player.getName() + " is not a hunter!");
                }
            } else {
                Hunter hunter = TargetManager.getHunters().get(player.getUniqueId());
                // if hunter has a tracking compass in their inventory
                if(TrackingCompassUtils.getTrackingCompass(hunter.getEntity())!=null) {
                    if (senderIsPlayer) {
                        sender.sendMessage("You already have a Tracking Compass!");
                    } else {
                        sender.sendMessage(player.getName() + " already has a Tracking Compass!");
                    }
                } else {
                    // if hunter has assigned compass, use it for setCompass()
                    if(hunter.getCompass()!=null){
                        hunter.setCompass(hunter.getCompass());
                    // else assign a new tracking compass
                    } else {
                        hunter.setCompass(TrackingCompassUtils.trackingCompass());
                    }
                    if (!senderIsPlayer) {
                        sender.sendMessage(player.getName() + " was given a Tracking Compass!");
                    }
                }
            }
        }
    }

    public static void track(CommandSender sender, Collection<Entity> targetsInit){
        Entity target = null;
        Player player = (Player) sender;
        Hunter hunter;
        if(!TargetManager.hasRole(player,RoleEnum.HUNTER)){
            player.sendMessage("You are not a hunter!");
            return;
        }
        hunter = TargetManager.getHunters().get(player.getUniqueId());
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
                if(entity instanceof Player && !TargetManager.getRunners().containsKey(entity.getUniqueId())){
                    continue;
                }
                double dist = player.getLocation().distanceSquared(entity.getLocation());
                if(target==null){
                    target = entity;
                    min = dist;
                }
                else if(dist<min){
                    target=entity;
                    min=dist;
                }
            }
        }
        if(target!=null){
            Target newTarget = new Target(target);
            hunter.setTarget(newTarget);
            player.sendMessage("Now tracking " + target.getName());
            hunter.updateCompass();

        } else {
            player.sendMessage("No targets found!");
        }
    }

    public static void recalibrate(CommandSender sender){
        Player player = (Player) sender;
        Hunter hunter = TargetManager.getHunters().get(player.getUniqueId());
        hunter.setTarget(null);
        hunter.updateCompass();
        player.sendMessage("Compass reset!");
    }
}
