package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.roles.*;
import me.rubataga.everyhunt.utils.CommandSenderMessenger;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TrackingService {

    public static void addRunnerCommand(CommandSender sender, Collection<Entity> runnerEntities){
        CommandSenderMessenger senderCsm = new CommandSenderMessenger(sender);
        for(Entity entity : runnerEntities){
            Player player = (Player) entity;
            try{
                addRunner(player);
                senderCsm.povMessage(player," now a runner!");
            } catch (EntityHasRoleException e){
                RoleEnum role = e.role;
                if(role==RoleEnum.RUNNER){
                    senderCsm.povMessageSenderOnly(player," already a runner!");
                } else if (role==RoleEnum.HUNTER){
                    senderCsm.povMessageSenderOnly(player," already a hunter!");
                }
            }
        }
    }

    public static Target addRunner(Player runnerPlayer) throws EntityHasRoleException {
        if(TrackingManager.hasRole(runnerPlayer,RoleEnum.RUNNER)){
            throw new EntityHasRoleException(runnerPlayer,RoleEnum.RUNNER);
        }
        if (!GameCfg.huntersCanBeRunners && TrackingManager.hasRole(runnerPlayer,RoleEnum.HUNTER)){
            throw new EntityHasRoleException(runnerPlayer,RoleEnum.HUNTER);
        }
        Target target = TrackingManager.getTarget(runnerPlayer);
        if(target == null){
            target = new Target(runnerPlayer,false);
        }
        TrackingManager.addRunner(runnerPlayer,target);
        return target;
    }

    public static void addHunterCommand(CommandSender sender, Collection<Player> hunterEntities){
        CommandSenderMessenger senderCsm = new CommandSenderMessenger(sender);
        for(Player player : hunterEntities){
//            Player player = (Player) entity;
            try{
                addHunter(player);
                senderCsm.povMessage(player," now a hunter!");
            } catch (EntityHasRoleException e){
                RoleEnum role = e.role;
                if(role==RoleEnum.HUNTER){
                    senderCsm.povMessageSenderOnly(player," already a hunter!");
                } else if (role==RoleEnum.RUNNER){
                    senderCsm.povMessageSenderOnly(player," already a runner!");
                }
            }
        }
    }

    public static Hunter addHunter(Player hunterPlayer) throws EntityHasRoleException {
        if (TrackingManager.hasRole(hunterPlayer,RoleEnum.HUNTER)){
            throw new EntityHasRoleException(hunterPlayer,RoleEnum.HUNTER);
        }
        if (!GameCfg.huntersCanBeRunners && TrackingManager.hasRole(hunterPlayer,RoleEnum.RUNNER)){
            throw new EntityHasRoleException(hunterPlayer,RoleEnum.RUNNER);
        }
        // not a hunter
        Hunter hunter = new Hunter(hunterPlayer);
        hunter.setTarget(null);
        TrackingCompassUtils.assignTrackingCompass(hunter);
        TrackingManager.addHunter(hunterPlayer,hunter);
        return hunter;
    }

    public static Target addTarget(Entity targetEntity){
        return addTarget(targetEntity,false);
    }

    public static Target addTarget(Entity targetEntity, boolean addRunner){
        Target target = TrackingManager.getTarget(targetEntity);
        if(target==null){
            target = new Target(targetEntity);
        }
        TrackingManager.addTarget(targetEntity,target);
        if((GameCfg.autoAddRunners || addRunner) && (targetEntity instanceof Player)){
            if(!TrackingManager.getRunnerList().contains(target)){
                TrackingManager.addRunner(targetEntity,target);
            }
        }
        return target;
    }

    public static void removeEntityCommand(CommandSender sender, Collection<Entity> entities){
        CommandSenderMessenger csm = new CommandSenderMessenger(sender);
        for(Entity entity : entities) {
            try{
                List<String> messages = removeEntity(entity);
                for(String string : messages){
                    csm.povMessage(entity,string);
                }
            } catch (EntityHasRoleException e){
                csm.povMessageSenderOnly(entity," neither a hunter, runner, nor target!");
            }
        }
        if(entities.size()>1){
            csm.message("All selected entities removed!.");
        }
    }

    public static List<String> removeEntity(Entity entity) throws EntityHasRoleException {
        List<String> messages = new LinkedList<>();
        boolean isTarget = TrackingManager.hasRole(entity, RoleEnum.TARGET);
        if (isTarget) {
            TrackingManager.removeTarget(entity);
            messages.add(" no longer a target!");
        }
        if (entity instanceof Player) {
            Player player = (Player) entity;
            boolean isRunner = TrackingManager.hasRole(player, RoleEnum.RUNNER);
            boolean isHunter = TrackingManager.hasRole(player, RoleEnum.HUNTER);
            if (isRunner) {
                TrackingManager.removeRunner(player);
                messages.add(" no longer a runner!");
            }
            if (isHunter) {
                TrackingManager.removeHunter(player);
                TrackingCompassUtils.removeTrackingCompasses(player.getInventory());
                messages.add(" no longer a hunter!");
            }
            if (!isTarget && !isRunner && !isHunter) {
                throw new EntityHasRoleException(entity, false);
            }
        } else if (!isTarget) {
            throw new EntityHasRoleException(entity, false);
        }
        return messages;
    }

    public static void teams(CommandSender sender){
        Hunter[] hunters = TrackingManager.getHunters().values().toArray(new Hunter[]{});
        Target[] targets = TrackingManager.getTargets().values().toArray(new Target[]{});
        Target[] runners = TrackingManager.getRunners().values().toArray(new Target[]{});
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

    private static void buildTeamString(StringBuilder s, EveryhuntRole[] entities){
        for (int i = 0; i<entities.length; i++) {
            if(i>0){
                s.append(", ");
            }
            s.append(entities[i]);
        }
        s.append(".");
    }

}
