package me.rubataga.manhunt.commands;
import dev.jorel.commandapi.CommandAPICommand;

public class CommandManager {

    public CommandManager() {}

    public static void register() {
        new CommandAPICommand("mh")
                .withSubcommand(TargetManagerCommands.addRunner())
                .withSubcommand(TargetManagerCommands.addRunnerSelf()).
                withSubcommand(TargetManagerCommands.addRunnerMultiple())
                .withSubcommand(TargetManagerCommands.addHunter())
                .withSubcommand(TargetManagerCommands.addHunterSelf())
                .withSubcommand(TargetManagerCommands.addHunterMultiple())
                .withSubcommand(TargetManagerCommands.removePlayer())
                .withSubcommand(TargetManagerCommands.removeEntity())
                .withSubcommand(TargetManagerCommands.removeSelf())
                .withSubcommand(TargetManagerCommands.teams())
                .withSubcommand(TrackingCompassCommands.compass())
                .withSubcommand(TrackingCompassCommands.compassSelf())
                .withSubcommand(TrackingCompassCommands.trackRunner())
                .withSubcommand(TrackingCompassCommands.trackEntity())
                .withSubcommand(TrackingCompassCommands.recalibrate())
                .register();
    }
}
