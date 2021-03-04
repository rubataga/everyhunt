package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.roles.*;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TargetService {

    public static void addRunner(CommandSender sender, Collection<Entity> runners){
        CommandSenderMessenger senderCsm = new CommandSenderMessenger(sender);
        for(Entity entity : runners){
            if(!(entity instanceof Player)){
                senderCsm.msg(entity.getName() + " not a player!");
            } else {
                Player player = (Player) entity;
                if (!TargetManager.hasRole(player,RoleEnum.RUNNER)) {
                    TargetManager.addRunner(new Target(player));
                    senderCsm.povMsg(player," now a runner!");
                } else {
                    senderCsm.povMsgSenderOnly(player," already a runner!");
                }
            }
        }
    }

    public static void addHunter(CommandSender sender, Collection<Entity> hunters){
        CommandSenderMessenger senderCsm = new CommandSenderMessenger(sender);
        for(Entity entity : hunters) {
            if (!(entity instanceof Player)) {
                senderCsm.msg(entity.getName() + " is not a player!");
            } else {
                Player player = (Player) entity;
                if (!TargetManager.hasRole(player,RoleEnum.HUNTER)) { // not a hunter
                    Hunter hunter = new Hunter(player);
                    TargetManager.addHunter(hunter);
                    hunter.setTarget(null);
                    TrackingCompassUtils.assignTrackingCompass(hunter);
                    senderCsm.povMsg(player," now a hunter!");
                } else {
                    senderCsm.povMsgSenderOnly(player," already a hunter!");
                }
            }
        }
    }

    public static void removeEntity(CommandSender sender, Collection<Entity> entities){
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        for(Entity entity : entities) {
            boolean isTarget = TargetManager.hasRole(entity, RoleEnum.TARGET);
            if (isTarget) {
                TargetManager.removeTarget(entity);
                csm.povMsg(entity," no longer a target!");
            }
            if(entity instanceof Player) {
                Player player = (Player) entity;
                boolean isRunner = TargetManager.hasRole(player,RoleEnum.RUNNER);
                boolean isHunter = TargetManager.hasRole(player,RoleEnum.HUNTER);
                if (isRunner) {
                    TargetManager.removeRunner(player);
                    csm.povMsg(entity," no longer a runner!");
                }
                if (isHunter) {
                    TargetManager.removeHunter(player);
                    TrackingCompassUtils.removeTrackingCompasses(player);
                    csm.povMsg(entity," no longer a hunter!");
                }
                if (!isTarget && !isRunner && !isHunter) {
                    csm.povMsgSenderOnly(entity, " not a hunter, runner, nor target!");
                }
            } else if (!isTarget){
                csm.povMsgSenderOnly(entity," neither a hunter, runner, nor target!");
            }
        }
        if(entities.size()>1){
            csm.msg("All selected entities checked.");
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

}
