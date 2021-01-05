package me.rubataga.manhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TargetManagerCommands {

    private TargetManagerCommands() {}

    public static CommandAPICommand addRunner(){
        return new CommandAPICommand("addrunner")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player runner = (Player) args[0];
                    if(!TargetManager.isRunner(runner)){
                        TargetManager.addRunner(runner);
                        sender.sendMessage(runner.getName() + " is now a runner!");
                    } else {
                        sender.sendMessage(runner.getName() + " is already a runner");
                    }
                });
    }

    public static CommandAPICommand addRunnerSelf(){
        return new CommandAPICommand("player")
                .executesPlayer((sender, args) -> {
                    if(!TargetManager.isRunner(sender)){
                        TargetManager.addRunner(sender);
                        sender.sendMessage("You are now a runner!");
                    } else {
                        sender.sendMessage("You are already a runner!");
                    }
                });
    }

    public static CommandAPICommand addRunnerMultiple(){
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> playerCollection = (Collection)args[0];
                    for(Entity entity : playerCollection){
                        if(!(entity instanceof Player)){
                            sender.sendMessage(entity.getName() + " is not a player!");
                            return;
                        }
                        Player player = (Player) entity;
                        if(!TargetManager.isRunner(player)){
                            TargetManager.addRunner(player);
                            sender.sendMessage(player.getName() + " is now a runner!");
                            player.sendMessage("You are now a runner!");
                        } else {
                            sender.sendMessage(player.getName() + " is already a runner!");
                        }
                    }
                });
    }

    public static CommandAPICommand addHunter(){
        return new CommandAPICommand("addhunter")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player player = (Player) args[0];
                    if(!TargetManager.isHunter(player)){
                        TargetManager.setTarget(player,null);
                        sender.sendMessage(player.getName() + " is now a hunter!");
                    } else {
                        sender.sendMessage(player.getName() + " is already a hunter!");
                    }
                });
    }

    public static CommandAPICommand addHunterMultiple(){
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> playerCollection = (Collection)args[0];
                    for(Entity entity : playerCollection){
                        if(!(entity instanceof Player)){
                            sender.sendMessage(entity.getName() + " is not a player!");
                            return;
                        }
                        Player player = (Player) entity;
                        if(!TargetManager.isHunter(player)){
                            TargetManager.setTarget(player,null);
                            sender.sendMessage(player.getName() + " is now a hunter!");
                            player.sendMessage("You are now a hunter!");
                        } else {
                            sender.sendMessage(player.getName() + " is already a hunter!");
                        }
                    }
                });
    }

    public static CommandAPICommand addHunterSelf(){
        return new CommandAPICommand("addhunter")
                .executesPlayer((sender, args) -> {
                    if(!TargetManager.isHunter(sender)){
                        TargetManager.setTarget(sender,null);
                        sender.sendMessage("You are now a hunter!");
                    } else {
                        sender.sendMessage("You are already a hunter!");
                    }
                });
    }

    public static CommandAPICommand removePlayer(){
        return new CommandAPICommand("remove")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player player = (Player) args[0];
                    if (!TargetManager.isTarget(player) && !TargetManager.isRunner(player) && !TargetManager.isHunter(player)) {
                        sender.sendMessage(player.getName() + " is neither a hunter, runner, nor target!");
                        player.sendMessage("You are neither a hunter, runner, nor target!");
                        return;
                    }
                    if (TargetManager.isTarget(player)) {
                        TargetManager.removeTarget(player);
                        sender.sendMessage(player.getName() + " is no longer a target!");
                        player.sendMessage("You are no longer a target!");
                    }
                    if (TargetManager.isRunner(player)) {
                        TargetManager.removeRunner(player);
                        sender.sendMessage(player.getName() + " is no longer a runner!");
                        player.sendMessage("You are no longer a runner!");
                    }
                    if (TargetManager.isHunter(player)) {
                        TargetManager.removeHunter(player);
                        sender.sendMessage(player.getName() + " is no longer a hunter!");
                        player.sendMessage("You are no longer a hunter!");
                    }
                });
    }

    public static CommandAPICommand removeEntity(){
        return new CommandAPICommand("remove")
                .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> entityArray = (Collection)args[0];
                    for(Entity entity : entityArray){
                        if (!TargetManager.isTarget(entity) && !TargetManager.isRunner((Player)entity) && !TargetManager.isHunter((Player)entity)) {
                            sender.sendMessage(entity.getName() + " is neither a target, runner, nor hunter.");
                        } else {
                            if(TargetManager.isTarget(entity)){
                                TargetManager.removeTarget(entity);
                                sender.sendMessage(entity.getName() + " is no longer a target!");
                                entity.sendMessage("You are no longer a target!");
                            }
                            if(entity instanceof Player){
                                if(TargetManager.isRunner((Player)entity)){
                                    TargetManager.removeRunner((Player) entity);
                                    sender.sendMessage(entity.getName() + " is no longer a runner!");
                                    entity.sendMessage("You are no longer a runner!");
                                }
                                if(TargetManager.isHunter((Player)entity)){
                                    TargetManager.removeHunter((Player)entity);
                                    sender.sendMessage(entity.getName() + " is no longer a hunter!");
                                    entity.sendMessage("You are no longer a hunter!");
                                }
                            }
                        }

                    }
                    sender.sendMessage("All entities checked.");
                });
    }

    public static CommandAPICommand removeSelf(){
        return new CommandAPICommand("remove")
                .executesPlayer((sender, args) -> {
                    if (!TargetManager.isTarget(sender) && !TargetManager.isRunner(sender) && !TargetManager.isHunter(sender)) {
                        sender.sendMessage("You are neither a hunter, runner, nor target!");
                        return;
                    }
                    if (TargetManager.isTarget(sender)) {
                        TargetManager.removeTarget(sender);
                        sender.sendMessage("You are no longer a target!");
                    }
                    if (TargetManager.isRunner(sender)) {
                        TargetManager.removeRunner(sender);
                        sender.sendMessage("You are no longer a runner!");
                    }
                    if (TargetManager.isHunter(sender)) {
                        TargetManager.removeHunter(sender);
                        sender.sendMessage("You are no longer a hunter!");
                    }
                });
    }

    public static CommandAPICommand teams(){
        return new CommandAPICommand("teams")
                .executes((sender,args) -> {
                    String targetsString = "";
                    String huntersString = "";
                    Player[] hunters = TargetManager.getTargets().keySet().toArray(new Player[]{});
                    Entity[] targets = TargetManager.getTargets().values().toArray(new Entity[]{});
                    if(hunters.length==0){
                        huntersString = "no hunters!";
                    } else {
                        for (Player hunter : hunters) {
                            huntersString += hunter.getName() + ", ";
                        }
                        huntersString = huntersString.substring(0,huntersString.length()-2) + ".";
                    }
                    if(targets.length==0) {
                        targetsString = "no targets!";
                    } else {
                        for (Entity target : targets) {
                            targetsString += target.getName() + ", ";
                        }
                        targetsString = targetsString.substring(0,targetsString.length()-2) + ".";
                    }

                    sender.sendMessage("§aHunters (" + hunters.length + "): " + huntersString);
                    sender.sendMessage("§aRunners (" + targets.length + "): " + targetsString);
                });
    }

}
