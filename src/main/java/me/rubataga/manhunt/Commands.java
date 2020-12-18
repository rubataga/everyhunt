package me.rubataga.manhunt;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.rubataga.manhunt.models.Hunter;
import org.bukkit.entity.Player;

import java.util.List;

public class Commands {

    public static void register() {

        new CommandAPICommand("manhunt")
                .withSubcommand(new CommandAPICommand("addrunner")
                        .withArguments(new PlayerArgument("runner"))
                        .executes((sender, args) -> {
                            Player runner = (Player) args[0];
                            if(!Manhunt.isHunter(runner) && !Manhunt.isRunner(runner)){
                                Manhunt.runners.add(runner);
                                sender.sendMessage(runner.getName() + " is now a runner!");
                            } else {
                                sender.sendMessage(runner.getName() + " is already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("addrunner")
                        .executesPlayer((sender, args) -> {
                            if(!Manhunt.isHunter(sender) && !Manhunt.isRunner(sender)){
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
                            if(!Manhunt.isRunner(player) && !Manhunt.isHunter(player)){
                                Hunter hunter = new Hunter(player);
                                Manhunt.hunters.put(player,hunter);
                                sender.sendMessage(player.getName() + " is now a hunter!");
                            } else {
                                sender.sendMessage(player.getName() + " is already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("addhunter")
                        .executesPlayer((sender, args) -> {
                            if(!Manhunt.isRunner(sender) && !Manhunt.isHunter(sender)){
                                Hunter hunter = new Hunter(sender);
                                Manhunt.hunters.put(sender,hunter);
                                sender.sendMessage("You are now a hunter!");
                            } else {
                                sender.sendMessage("You are already a runner or hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new PlayerArgument("player"))
                        .executes((sender, args) -> {
                            Player player = (Player) args[0];
                            if(Manhunt.isRunner(player)){
                                Manhunt.runners.remove(player);
                                sender.sendMessage(player.getName() + " is no longer a runner!");

                            }
                             else if(Manhunt.isHunter(player)){
                                 Manhunt.hunters.remove(player);
                                 sender.sendMessage(player.getName() + " is no longer a hunter!");
                            } else {
                                 sender.sendMessage(player.getName() + " neither a runner nor hunter!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("compass")
                        .withArguments(new PlayerArgument("target"))
                        .executes((sender, args) -> {
                            Player target = (Player) args[0];
                            if (Manhunt.giveCompass(target)) {
                                sender.sendMessage(target.getName() + " was given a compass.");
                            } else {
                                sender.sendMessage(target.getName() + " was not given a compass.");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("compass")
                        .executesPlayer((sender, args) -> {
                            if (Manhunt.giveCompass(sender)) {
                                sender.sendMessage("You were given a compass.");
                            } else {
                                sender.sendMessage("You were not given a compass.");
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("teams")
                    .executes((sender,args) -> {
                        String runnersString = "";
                        String huntersString = "";
                        for(Player runner : Manhunt.runners) {
                            runnersString += runner.getName() + ", ";
                        }
                        for(Player hunter : Manhunt.hunters.keySet()) {
                            huntersString += hunter.getName() + ", ";
                        }
                        //huntersString = huntersString.substring(0,huntersString.length()-2) + ".";
                        //runnersString = runnersString.substring(0,runnersString.length()-2) + ".";
                        if(Manhunt.runners.size()==0){
                            runnersString = "no runners!";
                        }
                        if(Manhunt.hunters.size()==0) {
                            huntersString = "no hunters!";
                        }
                        sender.sendMessage("§aHunters (" + Manhunt.hunters.size() + "): " + huntersString);
                        sender.sendMessage("§aRunners (" + Manhunt.runners.size() + "): " + runnersString);
                    }))
                .register();

    }
}
