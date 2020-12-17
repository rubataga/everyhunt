package me.rubataga.manhunt;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

public class Commands {

    public static void register() {

        new CommandAPICommand("manhunt")
                .withSubcommand(new CommandAPICommand("add")
                        .withArguments(new PlayerArgument("runner"))
                        .executes((sender, args) -> {
                            Player runner = (Player) args[0];
                            if(!Manhunt.isRunner(runner)){
                                Manhunt.runners.add(runner);
                                sender.sendMessage(runner.getName() + " is now a runner!");
                            } else {
                                sender.sendMessage(runner.getName() + " is already a runner!");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new PlayerArgument("runner"))
                        .executes((sender, args) -> {
                            Player runner = (Player) args[0];
                            if(!Manhunt.isRunner(runner)){
                                sender.sendMessage(runner.getName() + " is not a runner!");
                            } else {
                                Manhunt.runners.remove(runner);
                                sender.sendMessage(runner.getName() + " is no longer a runner!");
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
                .register();

    }
}
