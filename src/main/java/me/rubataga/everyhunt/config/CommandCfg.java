package me.rubataga.everyhunt.config;

import dev.jorel.commandapi.CommandAPICommand;
import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.commands.AdminCommands;
import me.rubataga.everyhunt.commands.TargetManagerCommands;
import me.rubataga.everyhunt.commands.TrackingCompassCommands;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing all of the plugin's commands
 */
public class CommandCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final FileConfiguration DEFAULT_CONFIG = EVERYHUNT.getConfig();
    private static final InputStream BASE_CONFIG_STREAM = EVERYHUNT.getResource("config.yml");
    private static final FileConfiguration BASE_CONFIG = new YamlConfiguration();
    private static final String COMMAND_PREFIX;
    private static final List<String> DISABLED_COMMANDS;

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

    private final static Map<CommandAPICommand,String> commands = new HashMap<>();
    private final static Map<CommandAPICommand,String> requiredCommands = new HashMap<>();

    static{
        try {
            BASE_CONFIG.load(new InputStreamReader(BASE_CONFIG_STREAM));
        } catch (IOException | InvalidConfigurationException ignore) {
        }

        String prefix;
        if(DEFAULT_CONFIG.contains("commandPrefix")){
            prefix = DEFAULT_CONFIG.getString("commandPrefix");
        } else {
            prefix = BASE_CONFIG.getString("commandPrefix");
        }
        COMMAND_PREFIX = prefix;
        List<String> disabled;
        if(DEFAULT_CONFIG.contains("disabledCommands")){
            disabled = DEFAULT_CONFIG.getStringList("disabledCommands");
        } else {
            disabled = BASE_CONFIG.getStringList("disabledCommands");
        }
        DISABLED_COMMANDS = disabled;

        //RequiredCommands
        requiredCommands.put(CONFIG,"config");
        requiredCommands.put(LOAD_CONFIG,"loadConfig");
        requiredCommands.put(CONFIG_GUI,"configGui");

        //AdminCommands
        commands.put(SUM,"sum");
        commands.put(SUM_SELF,"sum");
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
        if(COMMAND_PREFIX!=null && !COMMAND_PREFIX.equals("")) {
            CommandAPICommand prefix = new CommandAPICommand(COMMAND_PREFIX);
            List<CommandAPICommand> enabledCommands = new ArrayList<>();
            for (CommandAPICommand c : commands.keySet()) {
                String commandName = commands.get(c);
                if (DISABLED_COMMANDS.contains(commandName)) {
                    Debugger.send("Disabled command: " + commandName);
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                enabledCommands.add(c);
            }
            for (CommandAPICommand c : requiredCommands.keySet()) {
                Debugger.send("Registering required command: " + c.getName());
                enabledCommands.add(c);
            }
            prefix.setSubcommands(enabledCommands);
            prefix.register();
        }
        else {
            for(CommandAPICommand c : commands.keySet()){
                if(DISABLED_COMMANDS.contains(commands.get(c))){
                    continue;
                }
                Debugger.send("Registering command: " + c.getName());
                c.register();
            }
            for (CommandAPICommand c : requiredCommands.keySet()) {
                Debugger.send("Registering required command: " + c.getName());
                c.register();
            }
        }
    }
}
