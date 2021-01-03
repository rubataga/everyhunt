package me.rubataga.manhunt;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.rubataga.manhunt.services.TargetManager;
import me.rubataga.manhunt.models.TrackingCompassUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Commands {

    public static void register() {

        new CommandAPICommand("mh")
                .withSubcommand(new CommandAPICommand("addrunner")
                        .withArguments(new PlayerArgument("runner"))
                        .executes((sender, args) -> {
                            Player runner = (Player) args[0];
                            if(!TargetManager.isHunter(runner) && !TargetManager.isRunner(runner)){
                                Manhunt.runners.add(runner);
                                sender.sendMessage(runner.getName() + " is now a runner!");
                            } else {
                                sender.sendMessage(runner.getName() + " is already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("addrunner")
                        .executesPlayer((sender, args) -> {
                            if(!TargetManager.isHunter(sender) && !TargetManager.isRunner(sender)){
                                Manhunt.runners.add(sender);
                                sender.sendMessage("You are now a runner!");
                            } else {
                                sender.sendMessage("You are already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("addhunter")
                        .withArguments(new PlayerArgument("hunter"))
                        .executes((sender, args) -> {
                            Player player = (Player) args[0];
                            if(!TargetManager.isRunner(player) && !TargetManager.isHunter(player)){
                                TargetManager.setTarget(player,null);
                                sender.sendMessage(player.getName() + " is now a hunter!");
                            } else {
                                sender.sendMessage(player.getName() + " is already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("addhunter")
                        .executesPlayer((sender, args) -> {
                            if(!TargetManager.isRunner(sender) && !TargetManager.isHunter(sender)){
                                TargetManager.setTarget(sender,null);
                                sender.sendMessage("You are now a hunter!");
                            } else {
                                sender.sendMessage("You are already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new PlayerArgument("player"))
                        .executes((sender, args) -> {
                            Player player = (Player) args[0];
                            if(TargetManager.isRunner(player)){
                                TargetManager.removeTarget(player);
                                TargetManager.removeRunner(player);
                                sender.sendMessage(player.getName() + " is no longer a runner!");

                            }
                             else if(TargetManager.isHunter(player)){
                                 TargetManager.removeHunter(player);
                                 sender.sendMessage(player.getName() + " is no longer a hunter!");
                            } else {
                                 sender.sendMessage(player.getName() + " neither a runner nor hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("compass")
                        .withArguments(new PlayerArgument("target"))
                        .executes((sender, args) -> {
                            Player target = (Player) args[0];
                            if (TrackingCompassUtils.giveTrackingCompass(target)) {
                                sender.sendMessage(target.getName() + " was given a compass.");
                            } else {
                                sender.sendMessage(target.getName() + " was not given a compass.");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("compass")
                        .executesPlayer((sender, args) -> {
                            if (TrackingCompassUtils.giveTrackingCompass(sender)) {
                                sender.sendMessage("You were given a compass.");
                            } else {
                                sender.sendMessage("You were not given a compass.");
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("trackRunner")
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
                        })
                )
                .withSubcommand(new CommandAPICommand("trackEntity")
                        .withArguments(new EntitySelectorArgument("entity", EntitySelectorArgument.EntitySelector.MANY_ENTITIES))
                        .executesPlayer((sender,args) -> {
                            if(((Collection)args[0]).size()>1){
                                sender.sendMessage("Too many possible targets!");
                            }
                            if(TargetManager.isHunter(sender)){
                                TargetManager.setTarget(sender, (Entity)((ArrayList)args[0]).get(0));
                                sender.sendMessage("You are now targeting " + ((Entity)((ArrayList)args[0]).get(0)).getName());
                            }
                            else {
                                sender.sendMessage("You are not a hunter!");
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("teams")
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
                    }))
                .register();

    }
}
