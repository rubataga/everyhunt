package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.config.Rules;
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

public class CompassService {

    public static void giveCompass(CommandSender sender, Collection<Entity> hunters){
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        for(Entity entity : hunters){
            Player player = (Player) entity;
            //boolean senderIsPlayer = sender == player;
            if(!TargetManager.hasRole(player, RoleEnum.HUNTER)){
                csm.povMsgSenderOnly(player," not a hunter!");
            } else {
                Hunter hunter = TargetManager.getHunter(player);
                // if hunter has a tracking compass in their inventory
                if(TrackingCompassUtils.assignTrackingCompass(hunter)){
                    csm.setYouString("You already have");
                    csm.setOtherString(" already has");
                    csm.povMsg(player," a Tracking Compass!");
                } else {
                    csm.setYouString("You were");
                    csm.setOtherString(" was");
                    csm.povMsg(player," given a Tracking Compass!",sender!=player,false);
                    hunter.updateCompassMeta();
                }
            }
        }
    }

    public static void track(CommandSender sender, Collection<Entity> targetsInit){
        Entity targetEntity = null;
        Target target;
        Player player = (Player) sender;
        Hunter hunter = TargetManager.getHunter(player);
        if(hunter==null){
            player.sendMessage("You are not a hunter!");
            return;
        }
        //delete yourself from entityList
        targetsInit.remove(player);
        if(targetsInit.size()>0){
            if(targetsInit.size()==1){
               targetEntity = targetsInit.toArray(new Entity[1])[0];
                target = TargetManager.getTarget(targetEntity);
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
                           player.sendMessage("Sorry, " + targetEntity.getName() + " has    not been in this world yet.");
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
            target = TargetManager.getTargets().get(targetEntity);
            if(target==null){
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
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        Player player = (Player) sender;
        Hunter hunter;
        if(!TargetManager.hasRole(player,RoleEnum.HUNTER)){
            csm.msg("You are not a hunter!");
            return;
        }
        hunter = TargetManager.getHunter(player);
        hunter.setTarget(null);
        hunter.setLastTracked(null);
        hunter.setTrackingDeath(false);
        hunter.setLodestoneTracking();
        hunter.setTrackingPortal();
        hunter.updateCompassMeta();
        csm.msg("Compass reset!");
    }

    public static void gui(CommandSender sender){
        Hunter hunter = TargetManager.getHunter((Entity)sender);
        if(hunter!=null){
            hunter.getGUI().show();
        } else {
            sender.sendMessage("Only hunters can do this!");
        }
    }
}