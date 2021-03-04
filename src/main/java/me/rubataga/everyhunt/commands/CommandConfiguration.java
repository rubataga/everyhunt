package me.rubataga.everyhunt.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.rubataga.everyhunt.game.GameCfg;
import me.rubataga.everyhunt.utils.Debugger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing all of the plugin's commands
 */
public class CommandConfiguration {

    public static final CommandAPICommand SUM = AdminCommands.sum();
    public static final CommandAPICommand SUM_SELF = AdminCommands.sumSelf();
    public static final CommandAPICommand CONFIG = AdminCommands.config();
    public static final CommandAPICommand CONFIG_GUI = AdminCommands.configGui();
    public static final CommandAPICommand DUMMY = AdminCommands.dummy();

    public static final CommandAPICommand ADD_RUNNER = TargetManagerCommands.addRunner();
    public static final CommandAPICommand ADD_RUNNER_SELF = TargetManagerCommands.addRunnerSelf();
    public static final CommandAPICommand ADD_RUNNER_MULTIPLE = TargetManagerCommands.addRunnerMultiple();
    public static final CommandAPICommand ADD_HUNTER = TargetManagerCommands.addHunter();
    public static final CommandAPICommand ADD_HUNTER_SELF = TargetManagerCommands.addHunterSelf();
    public static final CommandAPICommand ADD_HUNTER_MULTIPLE = TargetManagerCommands.addHunterMultiple();
    public static final CommandAPICommand REMOVE_PLAYER = TargetManagerCommands.removePlayer();
    public static final CommandAPICommand REMOVE_ENTITY = TargetManagerCommands.removeEntity();
    public static final CommandAPICommand REMOVE_SELF = TargetManagerCommands.removeSelf();
    public static final CommandAPICommand TEAMS = TargetManagerCommands.teams();

    public static final CommandAPICommand COMPASS = TrackingCompassCommands.compass();
    public static final CommandAPICommand COMPASS_SELF = TrackingCompassCommands.compassSelf();
    public static final CommandAPICommand TRACK_RUNNER = TrackingCompassCommands.trackRunner();
    public static final CommandAPICommand TRACK_ENTITY = TrackingCompassCommands.trackEntity();
    public static final CommandAPICommand RESET = TrackingCompassCommands.reset();
    public static final CommandAPICommand GUI = TrackingCompassCommands.gui();

    public final static Map<CommandAPICommand,String> commands = new HashMap<>();
    public final static List<String> disabledCommands = GameCfg.disabledCommands;


    static{
                //AdminCommands
                commands.put(SUM,"sum");
                commands.put(SUM_SELF,"sum");
                commands.put(CONFIG,"config");
                commands.put(CONFIG_GUI,"configGui");
                commands.put(DUMMY,"dummy");

                //TargetManagerCommands
                commands.put(ADD_RUNNER,"addrunner");
                commands.put(ADD_RUNNER_SELF,"addrunner");
                commands.put(ADD_RUNNER_MULTIPLE,"addrunner");
                commands.put(ADD_HUNTER,"addhunter");
                commands.put(ADD_HUNTER_SELF,"addhunter");
                commands.put(ADD_HUNTER_MULTIPLE,"addhunter");
                commands.put(REMOVE_PLAYER,"remove");
                commands.put(REMOVE_ENTITY,"remove");
                commands.put(REMOVE_SELF,"remove");
                commands.put(TEAMS,"teams");

                //TrackingCompassCommands
                commands.put(COMPASS,"compass");
                commands.put(COMPASS_SELF,"compass");
                commands.put(TRACK_RUNNER,"track");
                commands.put(TRACK_ENTITY,"track");
                commands.put(RESET,"reset");
                commands.put(GUI,"gui");
    }

    /**
     * Registers all of the plugin's commands
     */
    public static void register() {
        String commandPrefix = GameCfg.commandPrefix;
        if(!commandPrefix.equals("")) {
            CommandAPICommand prefix = new CommandAPICommand(commandPrefix);
            List<CommandAPICommand> enabledCommands = new ArrayList<>();
            for(CommandAPICommand c : commands.keySet()){
                String commandName = commands.get(c);
                if(disabledCommands.contains(commandName)){
                    Debugger.send("Disabled command: " + commandName);
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                enabledCommands.add(c);
            }
            prefix.setSubcommands(enabledCommands);
            prefix.register();
        }
        else {
            for(CommandAPICommand c : commands.keySet()){
                if(disabledCommands.contains(commands.get(c))){
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                c.register();
            }
        }
    }
}
