package me.rubataga.everyhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * Class containing all of the plugin's commands
 */
public class CommandConfiguration {

    /**
     * Registers all of the plugin's commands
     */
    public static void register() {
        new CommandAPICommand("eh")

                //AdminCommands
                .withSubcommand(AdminCommands.sum())
                .withSubcommand(AdminCommands.sumSelf())
                .withSubcommand(AdminCommands.config())

                //TargetManagerCommands
                .withSubcommand(TargetManagerCommands.addRunner())
                .withSubcommand(TargetManagerCommands.addRunnerSelf())
                .withSubcommand(TargetManagerCommands.addRunnerMultiple())
                .withSubcommand(TargetManagerCommands.addHunter())
                .withSubcommand(TargetManagerCommands.addHunterSelf())
                .withSubcommand(TargetManagerCommands.addHunterMultiple())
                .withSubcommand(TargetManagerCommands.removePlayer())
                .withSubcommand(TargetManagerCommands.removeEntity())
                .withSubcommand(TargetManagerCommands.removeSelf())
                .withSubcommand(TargetManagerCommands.teams())

                //TrackingCompassCommands
                .withSubcommand(TrackingCompassCommands.compass())
                .withSubcommand(TrackingCompassCommands.compassSelf())
                .withSubcommand(TrackingCompassCommands.trackRunner())
                .withSubcommand(TrackingCompassCommands.trackEntity())
                .withSubcommand(TrackingCompassCommands.reset())
                .withSubcommand(TrackingCompassCommands.gui())

                .register();

    }
}
