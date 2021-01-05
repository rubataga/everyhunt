package me.rubataga.manhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.rubataga.manhunt.models.TrackingCompassUtils;
import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class TrackingCompassCommands {

    public TrackingCompassCommands() {}

    public static CommandAPICommand compass(){
        return new CommandAPICommand("compass")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player target = (Player) args[0];
                    if (TrackingCompassUtils.giveTrackingCompass(target)) {
                        sender.sendMessage(target.getName() + " was given a compass.");
                    } else {
                        sender.sendMessage(target.getName() + " was not given a compass.");
                    }
                });
    }

    public static CommandAPICommand compassSelf(){
        return new CommandAPICommand("compass")
                .executesPlayer((sender, args) -> {
                    if (TrackingCompassUtils.giveTrackingCompass(sender)) {
                        sender.sendMessage("You were given a compass.");
                    } else {
                        sender.sendMessage("You were not given a compass.");
                    }
                });
    }

    public static CommandAPICommand trackRunner(){
        return new CommandAPICommand("track")
                .withArguments(new PlayerArgument("runner"))
                .executesPlayer((sender, args) -> {
                    if(TargetManager.isHunter(sender)){
                        if(TargetManager.isRunner((Player) args[0])){
                            TargetManager.setTarget(sender, (Player) args[0]);
                        }
                        else {
                            sender.sendMessage("That player is not a runner!");
                        }
                    }
                    else {
                        sender.sendMessage("You are not a hunter!");
                    }
                });
    }

    public static CommandAPICommand trackEntity(){
        return new CommandAPICommand("track")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executesPlayer((sender,args) -> {
                    if(!TargetManager.isHunter(sender)){
                        sender.sendMessage("You are not a hunter!");
                        return;
                    }
                    List<Entity> entityList = (LinkedList)args[0];
                    if(entityList.size()==0){
                        sender.sendMessage("No targets found!");
                        return;
                    }
                    Entity target = entityList.get(0);
                    if(entityList.size()>1){
                        sender.sendMessage("Multiple targets found! Tracking closest target.");
                        double min = sender.getLocation().distanceSquared(entityList.get(0).getLocation());
                        for(Entity entity : entityList){
                            double dist = sender.getLocation().distanceSquared(entity.getLocation());
                            if(dist<min){
                                target=entity;
                                min=dist;
                            }
                        }
                    }
                    if(TargetManager.isHunter(sender)){
                        TargetManager.setTarget(sender, target);
                        sender.sendMessage("Now tracking " + target.getName());
                    }
                });
    }

    public static CommandAPICommand recalibrate(){
        return new CommandAPICommand("recalibrate")
                .withAliases("recompass","recal","rc")
                .executesPlayer((sender, args) -> {
                    TargetManager.setTarget(sender,null);
                    TargetManager.removeHunterTrackingDeath(sender);
                    sender.sendMessage("Compass reset!");
                });
    }

}
