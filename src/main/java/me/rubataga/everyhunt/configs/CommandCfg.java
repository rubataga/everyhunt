package me.rubataga.everyhunt.configs;

import dev.jorel.commandapi.CommandAPICommand;
import me.rubataga.everyhunt.commands.AdminCommands;
import me.rubataga.everyhunt.commands.LobbyCommands;
import me.rubataga.everyhunt.commands.TrackingCommands;
import me.rubataga.everyhunt.commands.HunterCommands;
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

    private static final CommandAPICommand NEW_GAME = LobbyCommands.newGame();
    private static final CommandAPICommand START_GAME = LobbyCommands.startGame();
    private static final CommandAPICommand STOP_GAME = LobbyCommands.stopGame();
    private static final CommandAPICommand JOIN_PLAYER = LobbyCommands.joinPlayer();
    private static final CommandAPICommand JOIN_PLAYER_MULTIPLE = LobbyCommands.joinPlayerMultiple();
    private static final CommandAPICommand JOIN_SELF = LobbyCommands.joinSelf();

    private static final CommandAPICommand ADD_RUNNER = TrackingCommands.addRunner();
    private static final CommandAPICommand ADD_RUNNER_SELF = TrackingCommands.addRunnerSelf();
    private static final CommandAPICommand ADD_RUNNER_MULTIPLE = TrackingCommands.addRunnerMultiple();
    private static final CommandAPICommand ADD_HUNTER = TrackingCommands.addHunter();
    private static final CommandAPICommand ADD_HUNTER_SELF = TrackingCommands.addHunterSelf();
    private static final CommandAPICommand ADD_HUNTER_MULTIPLE = TrackingCommands.addHunterMultiple();
    private static final CommandAPICommand REMOVE_PLAYER = TrackingCommands.removePlayer();
    private static final CommandAPICommand REMOVE_PLAYER_MULTIPLE = TrackingCommands.removePlayerMultiple();
    private static final CommandAPICommand REMOVE_ENTITY = TrackingCommands.removeEntity();
    private static final CommandAPICommand REMOVE_ENTITY_MULTIPLE = TrackingCommands.removeEntityMultiple();
    private static final CommandAPICommand REMOVE_SELF = TrackingCommands.removeSelf();
    private static final CommandAPICommand TEAMS = TrackingCommands.teams();

    private static final CommandAPICommand COMPASS = HunterCommands.compass();
    private static final CommandAPICommand COMPASS_SELF = HunterCommands.compassSelf();
    private static final CommandAPICommand TRACK_RUNNER = HunterCommands.trackRunner();
    private static final CommandAPICommand TRACK_ENTITY = HunterCommands.trackEntity();
    private static final CommandAPICommand RESET = HunterCommands.reset();
    private static final CommandAPICommand GUI = HunterCommands.gui();

    private static final Map<CommandAPICommand,String> COMMANDS = new LinkedHashMap<>();
    private static final Map<CommandAPICommand,String> REQUIRED_COMMANDS = new HashMap<>();

    static{

        //RequiredCommands
        REQUIRED_COMMANDS.put(CONFIG,"config");
        REQUIRED_COMMANDS.put(LOAD_CONFIG,"loadconfig");
        REQUIRED_COMMANDS.put(CONFIG_GUI,"configgui");
        REQUIRED_COMMANDS.put(NEW_GAME,"newgame");
        REQUIRED_COMMANDS.put(START_GAME,"startgame");
        REQUIRED_COMMANDS.put(STOP_GAME,"stopgame");
        REQUIRED_COMMANDS.put(JOIN_PLAYER,"join");
        REQUIRED_COMMANDS.put(JOIN_PLAYER_MULTIPLE,"join");
        REQUIRED_COMMANDS.put(ADD_RUNNER,"addrunner");
        REQUIRED_COMMANDS.put(ADD_RUNNER_MULTIPLE,"addrunner");
        REQUIRED_COMMANDS.put(ADD_HUNTER,"addhunter");
        REQUIRED_COMMANDS.put(ADD_HUNTER_MULTIPLE,"addhunter");
        REQUIRED_COMMANDS.put(REMOVE_PLAYER,"remove");
        REQUIRED_COMMANDS.put(REMOVE_PLAYER_MULTIPLE,"remove");
//        REQUIRED_COMMANDS.put(REMOVE_ENTITY,"remove");
        REQUIRED_COMMANDS.put(REMOVE_ENTITY_MULTIPLE,"remove");
        REQUIRED_COMMANDS.put(COMPASS,"compass");

        //AdminCommands
        COMMANDS.put(SUM,"sum");
        COMMANDS.put(SUM_SELF,"sum");
        COMMANDS.put(DUMMY,"dummy");

        //GameEngineCommands
        COMMANDS.put(JOIN_SELF,"join");

        //TargetManagerCommands
        COMMANDS.put(ADD_RUNNER_SELF,"addrunner");
        COMMANDS.put(ADD_HUNTER_SELF,"addhunter");
        COMMANDS.put(REMOVE_SELF,"remove");
        COMMANDS.put(TEAMS,"teams");

        //TrackingCompassCommands
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
        Debugger.send("-------------- Registering Commands -------------- ");
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
