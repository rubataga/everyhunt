package me.rubataga.manhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Class containing commands relating to {@link TargetManager}
 */

public class TargetManagerCommands {

    private TargetManagerCommands() {}

    /**
     * Command to add player as runner
     *
     * @return CommandAPICommand
     */

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

    /**
     * Command to add self as runner
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand addRunnerSelf(){
        return new CommandAPICommand("addrunner")
                .executesPlayer((sender, args) -> {
                    if(!TargetManager.isRunner(sender)){
                        TargetManager.addRunner(sender);
                        sender.sendMessage("You are now a runner!");
                    } else {
                        sender.sendMessage("You are already a runner!");
                    }
                });
    }

    /**
     * Command to add multiple players as runners
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand addRunnerMultiple(){
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    // AP CSA : Casting
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

    /**
     * Command to add entity as hunter
     *
     * @return CommandAPICommand
     */

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

    /**
     * Command to add multiple entities as hunters
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand addHunterMultiple(){
        return new CommandAPICommand("addhunter")
                .withArguments(new EntitySelectorArgument("players", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                .executes((sender, args) -> {
                    Collection<Entity> playerCollection = (Collection)args[0];
                    for(Entity entity : playerCollection){
                        if(!(entity instanceof Player)){
                            sender.sendMessage(entity.getName() + " is not a player!");
                        } else {
                            Player player = (Player) entity;
                            if (!TargetManager.isHunter(player)) {
                                TargetManager.setTarget(player, null);
                                sender.sendMessage(player.getName() + " is now a hunter!");
                                player.sendMessage("You are now a hunter!");
                            } else {
                                sender.sendMessage(player.getName() + " is already a hunter!");
                            }
                        }
                    }
                });
    }

    /**
     * Command to add self as hunter
     *
     * @return CommandAPICommand
     */

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

    /**
     * Remove player as hunter, target, and/or runner
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand removePlayer(){
        return new CommandAPICommand("remove")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player player = (Player) args[0];
                    boolean isTarget = TargetManager.isTarget(player);
                    boolean isRunner = TargetManager.isRunner(player);
                    boolean isHunter = TargetManager.isHunter(player);

                    if (isTarget) {
                        TargetManager.removeTarget(player);
                        sender.sendMessage(player.getName() + " is no longer a target!");
                        player.sendMessage("You are no longer a target!");
                    }
                    if (isRunner) {
                        TargetManager.removeRunner(player);
                        sender.sendMessage(player.getName() + " is no longer a runner!");
                        player.sendMessage("You are no longer a runner!");
                    }
                    if (isHunter) {
                        TargetManager.removeHunter(player);
                        sender.sendMessage(player.getName() + " is no longer a hunter!");
                        player.sendMessage("You are no longer a hunter!");
                    }
                    if (!isTarget && !isRunner && !isHunter) {
                        sender.sendMessage(player.getName() + " is neither a hunter, runner, nor target!");
                        player.sendMessage("You are neither a hunter, runner, nor target!");
                    }
                });
    }

    /**
     * Remove entity as target
     *
     * @return CommandAPICommand
     */

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

    /**
     * Remove self as hunter, target, and/or runner
     *
     * @return CommandAPICommand
     */

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

    /**
     * List teams
     *
     * @return CommandAPICommand
     */

    public static CommandAPICommand teams(){
        return new CommandAPICommand("teams")
                .executes((sender,args) -> {
                    String targetsString = "";
                    String huntersString = "";
                    // AP CSA : Arrays
                    Player[] hunters = TargetManager.getHunterTargetMap().keySet().toArray(new Player[]{});
                    Entity[] targets = TargetManager.getHunterTargetMap().values().toArray(new Entity[]{});
                    if(hunters.length==0){
                        huntersString = "no hunters!";
                    } else {
                        // AP CSA : For each loop
                        // AP CSA: String concatenation with +=
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
