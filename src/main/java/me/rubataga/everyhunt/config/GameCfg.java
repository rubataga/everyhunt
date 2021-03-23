package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GameCfg extends AbstractCfg{

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final String EMBEDDED_GAMEMODE_NAME = "base.yml";

    private static ConfigGui gui;
    private static EmbeddedYamlEditor editor;

    public static boolean debugMode;
    public static String debugTag;
    public static String gameName;
    public static List<String> gameDescription;

    public static boolean useBlacklist;
    public static List<String> compassBlacklist;
    public static List<String> compassWhitelist;
    public static String blacklistMessage;

    public static boolean rightClickCyclesRunners;
    public static boolean rightClickResetsDeadCompass;

    public static boolean guiEnabled;
    public static boolean disablePortals;
    public static boolean huntersCanBeRunners;
    public static boolean nonRunnersCanBeTargeted;
    public static boolean autoAddRunners;
    public static boolean autoRemoveRunners;

    // load the default gamemode from config.yml
    public static void initialize() {
        String defaultName = PluginCfg.defaultGamemode;
        if (defaultName == null) {
            defaultName = "base.yml";
        }
        editor = new EmbeddedYamlEditor(GameCfg.class, EVERYHUNT, EMBEDDED_GAMEMODE_NAME, defaultName);
        EVERYHUNT.saveDefaultConfig();
        ConfigGui.initialize();
        gui = new ConfigGui();
        Debugger.setToGameCfg();
    }

    public static Object getValue(String key) {
        return getValue(key,GameCfg.class);
    }

    public static String getFormattedValue(String key){
        return getFormattedValue(key,GameCfg.class);
    }

    public static void load(String fileName) throws FileNotFoundException {
        editor.load(fileName);
    }

    public static Map<String,Field> getKeyFields(){
        return editor.getKeyFields();
    }


    public static ConfigGui getGui(){
        return gui;
    }

}
