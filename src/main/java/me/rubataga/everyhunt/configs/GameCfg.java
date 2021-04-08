package me.rubataga.everyhunt.configs;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.EmbeddedYamlEditor;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GameCfg{

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final String EMBEDDED_GAMEMODE_NAME = "base.yml";
    private static final Class<GameCfg> GAME_CFG_CLASS = GameCfg.class;

    private static ConfigGui gui;
    private static EmbeddedYamlEditor editor;

    public static String gameName;
    public static List<String> gameDescription;
    public static boolean autoStartOnLoad;
    public static int minimumPlayers;
    public static int minimumRunners;
    public static boolean playersCanMidjoin;
    public static boolean autoAddToGame;

    public static boolean useBlacklist;
    public static String blacklistMessage;
    public static List<String> compassBlacklist;
    public static List<String> compassWhitelist;

    public static boolean rightClickCyclesRunners;
    public static boolean rightClickResetsDeadCompass;
    public static boolean guiEnabled;

    public static boolean disablePortals;

    public static boolean assignRolesAtStart;
    public static String assignRoleAtMidjoin;
    public static boolean playersCanChangeRole;

    public static boolean assignTargetAtStart;
    public static boolean huntersCanChangeTarget;
    public static boolean huntersCanBeRunners;
    public static boolean huntersCanTrackDeadTargets;

    public static boolean nonRunnersCanBeTargeted;
    public static boolean autoAddRunners;
    public static boolean autoRemoveRunners;

    // load the default gamemode from config.yml
    public static void initialize() {
        String defaultName = PluginCfg.defaultGamemode;
        if (defaultName == null) {
            defaultName = "base.yml";
        }
        editor = new EmbeddedYamlEditor(GAME_CFG_CLASS, EVERYHUNT, EMBEDDED_GAMEMODE_NAME, defaultName);
        EVERYHUNT.saveDefaultConfig();
        ConfigGui.initialize();
        gui = new ConfigGui();
    }

    public static Object getValue(String key) {
        try {
            return GAME_CFG_CLASS.getField(key).get(GAME_CFG_CLASS);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
        return null;
    }

    public static String getFormattedValue(String key){
        return String.format("%s: %s",key,getValue(key));
    }

    public static void load(String fileName) throws FileNotFoundException {
        editor.load(fileName);
        for(String key : getKeyFields().keySet()){
            Debugger.send(getFormattedValue(key));
        }
    }

    public static Map<String,Field> getKeyFields(){
        return editor.getKeyFields();
    }

    public static ConfigGui getGui(){
        return gui;
    }

}
