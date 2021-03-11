package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.yamleditor.YamlEditor;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GameCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final String EMBEDDED_GAMEMODE_NAME = "base.yml";
    private static ConfigGui GUI;

    private static YamlEditor editor;

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
        editor = new YamlEditor(GameCfg.class, EVERYHUNT, defaultName, EMBEDDED_GAMEMODE_NAME);
        editor.load(defaultName);
        Debugger.setToGameCfg();
        EVERYHUNT.saveDefaultConfig();
        GUI = new ConfigGui();
    }

    public static Object getValue(String key){
        return editor.getValue(key);
    }

    public static void load(String fileName){
        editor.load(fileName);
    }

    public static String getFormattedValue(String key){
        return editor.getFormattedValue(key);
    }

    public static Map<String,Field> getFields(){
        return editor.getFields();
    }

    public static boolean isBlacklisted(Entity entity){
        String entityKey = entity.getType().getKey().getKey();
        if(useBlacklist){
            for(String key : compassBlacklist){
                Debugger.send("CHECKING BLACKLIST KEY: " + key);
                if(entityKey.equals(key)){
                    return true;
                }
            }
            return false;
        } else {
            for(String key : compassWhitelist){
                Debugger.send("CHECKING WHITELIST KEY: " + key);
                if(entityKey.equals(key)){
                    return false;
                }
            }
            return true;
        }
    }

    public static ConfigGui getGui(){
        return GUI;
    }

}
