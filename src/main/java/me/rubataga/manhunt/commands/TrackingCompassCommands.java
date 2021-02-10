package me.rubataga.manhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.rubataga.manhunt.models.TrackingCompassUtils;
import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class containing commands relating to {@link TrackingCompassUtils}
 */

public class TrackingCompassCommands {

    private TrackingCompassCommands() {}

    /**
     * Command to give a player a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @return CommandAPICommand
     */

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

    /**
     * Command to give self a {@link TrackingCompassUtils#trackingCompass()}
     *
     * @return CommandAPICommand
     */

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

    /**
     * Command given by player to track a runner
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand trackRunner(){
        return new CommandAPICommand("track")
                .withArguments(new PlayerArgument("runner"))
                .executesPlayer((sender, args) -> {
                    if(sender == args[0]){
                        sender.sendMessage("You can't track yourself!");
                        return;
                    }
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

    /**
     * Command given by player to track an entity / multiple entities
     *
     * @return CommandAPICommand
     */

    @SuppressWarnings("unchecked")
    public static CommandAPICommand trackEntity(){
        return new CommandAPICommand("track")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executesPlayer((sender,args) -> {
                    Bukkit.getLogger().warning("blah");
                    Bukkit.getLogger().warning(args[0].getClass().getName());

                    Collection<Entity> entityList = new ArrayList<>();
                    Entity target = null;

                    if(args[0] instanceof Collection){
                        entityList = (Collection<Entity>)args[0];
                    }
                    if(!TargetManager.isHunter(sender)){
                        sender.sendMessage("You are not a hunter!");
                        return;
                    }
                    //delete yourself from entityList
                    entityList.remove(sender);

                    if(entityList.size()>0){
                        if(entityList.size()>1){
                            sender.sendMessage("Multiple targets found! Tracking closest target.");
                        }
                        Double min = null;
                        //find closest target
                        for(Entity entity : entityList){
                            double dist = sender.getLocation().distanceSquared(entity.getLocation());
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
                        TargetManager.setTarget(sender, target);
                        sender.sendMessage("Now tracking " + target.getName());
                    } else {
                        sender.sendMessage("No targets found!");
                    }
                });
    }


    /**
     * Command to reset a player's compass location
     *
     * @return CommandAPICommand
     */

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
