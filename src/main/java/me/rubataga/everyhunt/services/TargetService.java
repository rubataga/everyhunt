package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.roles.*;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TargetService {

    public static void addRunner(CommandSender sender, Collection<Entity> runners){
        for(Entity entity : runners){
            if(!(entity instanceof Player)){
                sender.sendMessage(entity.getName() + " is not a player!");
            } else {
                Player player = (Player) entity;
                boolean senderIsPlayer = sender == player;
                if (!TargetManager.hasRole(player,RoleEnum.RUNNER)) {
                    TargetManager.addRunner(new Target(player));
                    player.sendMessage("You are now a runner!");
                    if (!senderIsPlayer) {
                        sender.sendMessage(player.getName() + " is now a runner!");
                    }
                } else {
                    if (senderIsPlayer) {
                        sender.sendMessage("You are already a runner!");
                    } else {
                        sender.sendMessage(player.getName() + " is already a runner!");
                    }
                }
            }
        }
    }

    public static void addHunter(CommandSender sender, Collection<Entity> hunters){
        for(Entity entity : hunters) {
            if (!(entity instanceof Player)) {
                sender.sendMessage(entity.getName() + " is not a player!");
            } else {
                Player player = (Player) entity;
                boolean senderIsPlayer = sender == player;
                if (!TargetManager.hasRole(player,RoleEnum.HUNTER)) { // not a hunter
                    Hunter hunter = new Hunter(player);
                    TargetManager.addHunter(hunter);
                    hunter.setTarget(null);
                    TrackingCompassUtils.assignTrackingCompass(hunter);
                    player.sendMessage("You are now a hunter!");
                    if (!senderIsPlayer) {
                        sender.sendMessage(player.getName() + " is now a hunter!");
                    }
                } else {
                    if (senderIsPlayer) {
                        player.sendMessage("You are already a hunter!");
                    } else {
                        sender.sendMessage(player.getName() + " is already a hunter!");
                    }
                }
            }
        }
    }

    public static void removeEntity(CommandSender sender, Collection<Entity> entities){
        for(Entity entity : entities) {
            boolean isPlayer = entity instanceof Player;
            boolean isTarget = TargetManager.hasRole(entity, RoleEnum.TARGET);
            boolean senderIsEntity = sender == entity;

            if (isTarget) {
                TargetManager.removeTarget(entity);
                if(isPlayer){
                    entity.sendMessage("You are no longer a target!");
                }
                if(!senderIsEntity){
                    sender.sendMessage(entity.getName() + " is no longer a target!");
                }
            }
            if(entity instanceof Player) {
                Player player = (Player) entity;
                boolean isRunner = TargetManager.hasRole(player,RoleEnum.RUNNER);
                boolean isHunter = TargetManager.hasRole(player,RoleEnum.HUNTER);
                if (isRunner) {
                    TargetManager.removeRunner(player);
                    if (senderIsEntity) {
                        player.sendMessage("You are no longer a runner!");
                    } else {
                        sender.sendMessage(player.getName() + " is no longer a runner!");
                    }
                }
                if (isHunter) {
                    TargetManager.removeHunter(player);
                    if (senderIsEntity) {
                        player.sendMessage("You are no longer a hunter!");
                    } else {
                        sender.sendMessage(player.getName() + " is no longer a hunter!");
                    }
                }
                if (!isTarget && !isRunner && !isHunter) {
                    if (senderIsEntity) {
                        player.sendMessage("You are neither a hunter, runner, nor target!");
                    } else {
                        sender.sendMessage(player.getName() + " is neither a hunter, runner, nor target!");
                    }
                }
            } else if (!isTarget){
                sender.sendMessage(entity.getName() + " is neither a hunter, runner, nor target!");
            }
        }
        if(entities.size()>1){
            sender.sendMessage("All selected entities checked.");
        }
    }

    public static void teams(CommandSender sender){
        Hunter[] hunters = TargetManager.getHunters().values().toArray(new Hunter[]{});
        Target[] targets = TargetManager.getTargets().values().toArray(new Target[]{});
        Target[] runners = TargetManager.getRunners().values().toArray(new Target[]{});
        StringBuilder huntersString = new StringBuilder("§aHunters (" + hunters.length + "): ");
        StringBuilder targetsString = new StringBuilder("§aTargets (" + targets.length + "): ");
        StringBuilder runnersString = new StringBuilder("§aRunners (" + runners.length + "): ");

        if(hunters.length==0){
            huntersString.append("no hunters!");
        } else {
            buildTeamString(huntersString,hunters);
        }
        if(targets.length==0) {
            targetsString.append("no targets!");
        } else {
            buildTeamString(targetsString,targets);
        }
        if(runners.length==0) {
            runnersString.append("no runners!");
        } else {
            buildTeamString(runnersString,runners);
        }
        sender.sendMessage(huntersString.toString());
        sender.sendMessage(targetsString.toString());
        sender.sendMessage(runnersString.toString());
    }

    private static void buildTeamString(StringBuilder s, EveryhuntEntity[] entities){
        for (int i = 0; i<entities.length; i++) {
            if(i>0){
                s.append(", ");
            }
            s.append(entities[i].getEntity().getName());
        }
        s.append(".");
    }

    public static void sum(CommandSender sender, Collection<Entity> entities){
        for(Entity entity : entities){
            if(TargetManager.getHunters().containsKey(entity)){
                Hunter hunter = TargetManager.getHunters().get(entity);
                sender.sendMessage("§b" + entity.getName());
                sender.sendMessage("Compass is null?: " + (hunter.getCompass()==null));
                sender.sendMessage("Tracked location: " + hunter.getEntity().getCompassTarget());
                if(hunter.getTarget()!=null){
                    sender.sendMessage("Target: " + hunter.getTargetEntity().getName());
                }
                sender.sendMessage("Tracking death: " + hunter.isTrackingDeath());
                sender.sendMessage("Tracking portal: " + hunter.isTrackingPortal());
                sender.sendMessage("Lodestone tracking: " + hunter.isLodestoneTracking());

            }
//            if(!TargetManager.getHunters().containsKey(entity) ||
//                    !TargetManager.getTargets().containsKey(entity) ||
//                    !TargetManager.getRunners().containsKey(entity)){
//                sender.sendMessage(entity.getName() + " had no roles.");
//            }
        }
    }


}
