package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.*;

public class GameCfg{

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final String EMBEDDED_GAMEMODE_NAME = "base.yml";
    private static final Map<String,Field> FIELDS = new LinkedHashMap<>();
    private static ConfigGui GUI;

    private static  YamlEditor editor;

    private static String loadedFileName = "";
    private static Map<String, Object> EMBEDDED_VALUE_MAP;
    private static Map<String,Object> loadedValueMap;
    private static Map<String,Object> defaultValueMap;

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

    static{
        for(Field f : GameCfg.class.getFields()){
            FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
    }

    // load the default gamemode from config.yml
    public static void initialize(){
        String defaultName = PluginCfg.defaultGamemode;
        if(defaultName==null){
            defaultName = "base.yml";
        }
        editor = new YamlEditor(GameCfg.class, EVERYHUNT, defaultName, EMBEDDED_GAMEMODE_NAME);
        EMBEDDED_VALUE_MAP = editor.embeddedValueMap;
        editor.load(defaultName);
        editor.setFieldsToValues();
        defaultValueMap = editor.defaultValueMap;
        EVERYHUNT.saveDefaultConfig();
        GUI = new ConfigGui();
    }

    public static void load(String fileName){
        if(loadedFileName.equalsIgnoreCase(fileName)){
            return;
        }
        editor.load(fileName);
        setValueMapToYamlEditor();
        editor.setFieldsToValues();
        loadedFileName = editor.loadedResourceName;
        Debugger.setToGameCfg();
    }

    // sets valueMap to the values from gamemodeInputStream
    public static void setValueMapToYamlEditor() {
        loadedValueMap = editor.loadedValueMap;
    }
    
    public static Map<String,Field> getFields(){
        return FIELDS;
    }

    // returns the value of a GameCfg public field
    public static Object getValue(String key) {
        Object value;
        if(FIELDS.containsKey(key)){
            Field f = FIELDS.get(key);
            try {
                value = f.get(GameCfg.class);
                Debugger.send(key + " returning as " + value);
                return value;
            }
            catch(IllegalAccessException e){
                Debugger.send("ERROR! Couldn't get value for " + key);
            }
        }
        value = loadedValueMap.get(key);
        if(value==null){
            Debugger.send("Couldn't find value! Using default value: " + value);
            value = defaultValueMap.get(key);
            if(value==null){
                value = EMBEDDED_VALUE_MAP.get(key);
                Debugger.send("Couldn't find value! Using base value: " + value);
            }
        }
        return value;
    }

    // returns a formatted string of a value accessible by getValue()
    public static String getFormattedValue(String key){
        return String.format("%s: %s",key,getValue(key));
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
