package me.rubataga.everyhunt.config;

import dev.jorel.commandapi.CommandAPICommand;
import me.rubataga.everyhunt.commands.AdminCommands;
import me.rubataga.everyhunt.commands.TargetManagerCommands;
import me.rubataga.everyhunt.commands.TrackingCompassCommands;
import me.rubataga.everyhunt.utils.Debugger;

import java.util.*;

/**
 * Class containing all of the plugin's commands
 */
public class CommandCfg {

    public static final String COMMAND_PREFIX = PluginCfg.commandPrefix;
    public static final List<String> DISABLED_COMMANDS = PluginCfg.disabledCommands;

    private static final CommandAPICommand SUM = AdminCommands.sum();
    private static final CommandAPICommand SUM_SELF = AdminCommands.sumSelf();
    private static final CommandAPICommand CONFIG = AdminCommands.config();
    private static final CommandAPICommand LOAD_CONFIG = AdminCommands.loadConfig();
    private static final CommandAPICommand CONFIG_GUI = AdminCommands.configGui();
    private static final CommandAPICommand DUMMY = AdminCommands.dummy();

    private static final CommandAPICommand ADD_RUNNER = TargetManagerCommands.addRunner();
    private static final CommandAPICommand ADD_RUNNER_SELF = TargetManagerCommands.addRunnerSelf();
    private static final CommandAPICommand ADD_RUNNER_MULTIPLE = TargetManagerCommands.addRunnerMultiple();
    private static final CommandAPICommand ADD_HUNTER = TargetManagerCommands.addHunter();
    private static final CommandAPICommand ADD_HUNTER_SELF = TargetManagerCommands.addHunterSelf();
    private static final CommandAPICommand ADD_HUNTER_MULTIPLE = TargetManagerCommands.addHunterMultiple();
    private static final CommandAPICommand REMOVE_PLAYER = TargetManagerCommands.removePlayer();
    private static final CommandAPICommand REMOVE_ENTITY = TargetManagerCommands.removeEntity();
    private static final CommandAPICommand REMOVE_SELF = TargetManagerCommands.removeSelf();
    private static final CommandAPICommand TEAMS = TargetManagerCommands.teams();

    private static final CommandAPICommand COMPASS = TrackingCompassCommands.compass();
    private static final CommandAPICommand COMPASS_SELF = TrackingCompassCommands.compassSelf();
    private static final CommandAPICommand TRACK_RUNNER = TrackingCompassCommands.trackRunner();
    private static final CommandAPICommand TRACK_ENTITY = TrackingCompassCommands.trackEntity();
    private static final CommandAPICommand RESET = TrackingCompassCommands.reset();
    private static final CommandAPICommand GUI = TrackingCompassCommands.gui();

    private static final Map<CommandAPICommand,String> COMMANDS = new LinkedHashMap<>();
    private static final Map<CommandAPICommand,String> REQUIRED_COMMANDS = new HashMap<>();

    static{

        //RequiredCommands
        REQUIRED_COMMANDS.put(CONFIG,"config");
        REQUIRED_COMMANDS.put(LOAD_CONFIG,"loadconfig");
        REQUIRED_COMMANDS.put(CONFIG_GUI,"configgui");

        //AdminCommands
        COMMANDS.put(SUM,"sum");
        COMMANDS.put(SUM_SELF,"sum");
        COMMANDS.put(DUMMY,"dummy");

        //TargetManagerCommands
        COMMANDS.put(ADD_RUNNER,"addrunner");
        COMMANDS.put(ADD_RUNNER_SELF,"addrunner");
        COMMANDS.put(ADD_RUNNER_MULTIPLE,"addrunner");
        COMMANDS.put(ADD_HUNTER,"addhunter");
        COMMANDS.put(ADD_HUNTER_SELF,"addhunter");
        COMMANDS.put(ADD_HUNTER_MULTIPLE,"addhunter");
        COMMANDS.put(REMOVE_PLAYER,"remove");
        COMMANDS.put(REMOVE_ENTITY,"remove");
        COMMANDS.put(REMOVE_SELF,"remove");
        COMMANDS.put(TEAMS,"teams");

        //TrackingCompassCommands
        COMMANDS.put(COMPASS,"compass");
        COMMANDS.put(COMPASS_SELF,"compass");
        COMMANDS.put(TRACK_RUNNER,"track");
        COMMANDS.put(TRACK_ENTITY,"track");
        COMMANDS.put(RESET,"reset");
        COMMANDS.put(GUI,"gui");

    }

    /**
     * Registers all of the plugin's commands
     */
    public static void register() {
        if(COMMAND_PREFIX!=null && !COMMAND_PREFIX.equals("")) {
            CommandAPICommand prefix = new CommandAPICommand(COMMAND_PREFIX);
            List<CommandAPICommand> enabledCommands = new ArrayList<>();
            for (CommandAPICommand c : COMMANDS.keySet()) {
                String commandName = COMMANDS.get(c);
                if (DISABLED_COMMANDS.contains(commandName)) {
                    Debugger.send("Disabled command: " + commandName);
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                enabledCommands.add(c);
            }
            for (CommandAPICommand c : REQUIRED_COMMANDS.keySet()) {
                Debugger.send("Registering required command: " + c.getName());
                enabledCommands.add(c);
            }
            prefix.setSubcommands(enabledCommands);
            prefix.register();
        }
        else {
            for(CommandAPICommand c : COMMANDS.keySet()){
                if(DISABLED_COMMANDS.contains(COMMANDS.get(c))){
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                c.register();
            }
            for (CommandAPICommand c : REQUIRED_COMMANDS.keySet()) {
                Debugger.send("Registering required command: " + c.getName());
                c.register();
            }
        }
    }
}
