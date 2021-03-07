package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class GameCfg {

    public static Map<String,Object> values = new HashMap<>();
    public static Map<String,Field> fields = new HashMap<>();
    private static ConfigGui GUI = new ConfigGui();

    private static InputStream gamemodeInputStream;
    private static final Yaml yaml = new Yaml();

    public static boolean debugMode;
    public static String gameName;
    public static String baseGame;
    public static List<String> gameDescription;

    public static String commandPrefix;
    public static List<String> disabledCommands;
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

    static{
        for(Field f : GamemodeCfg.class.getFields()){
            fields.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
    }

    public static void setInputStream(String fileName) {
        File dataFolder = Everyhunt.getInstance().getDataFolder();
        File configFile = new File(dataFolder,fileName);
        try{
            gamemodeInputStream = configFile.toURI().toURL().openStream();
            Debugger.send("Static input stream: " + gamemodeInputStream);
        } catch (IOException e){
            Debugger.send("ERROR! gamemodeInputStream was not updated!");
        }
    }

    public static void setValues() {
        Debugger.send("Initial values map: " + values);
        values = yaml.load(gamemodeInputStream);
        Debugger.send("Final values map: " + values);
    }

    public static void loadValues() {
        Debugger.send("Setting static values!");
        for(String key : values.keySet()){
            Debugger.send("Editing key " + key);
            Field f = fields.get(key);
            if(f!=null){
                Debugger.send("Field name: " + f.getName());
                Object obj = values.get(key);
                Debugger.send("Value: " + obj);
                try {
                    f.set(GamemodeCfg.class, obj);
                    Debugger.send("Static set: " + key + " = " + obj);
                } catch(IllegalAccessException set) {
                    Debugger.send("ERROR! Failed to set key: " + key);
                }
                try{
                    Object check = f.get(GamemodeCfg.class);
                    Debugger.send("Static check: " + key + " = " + check);
                } catch(IllegalAccessException check){
                    Debugger.send("ERROR! Failed to set or check key: " + key);
                }
            }
        }
        Debugger.send("Loaded gamemode " + gameName);
    }

    public static Object getValue(String key) {
        Object value;
        if(fields.containsKey(key)){
            Field f = fields.get(key);
            try {
                value = f.get(GamemodeCfg.class);
                Debugger.send(key + " returning as " + value);
                return value;
            }
            catch(IllegalAccessException e){
                Debugger.send("ERROR! Couldn't get value for " + key);
            }
        }
        return null;
//        //        if(values.containsKey(key)){
////            value = values.get(key);
////            Debugger.send(key + " returning as " + value);
////            return value;
////        }
//        else {
//            value = defaultValues.get(key);
//        }
    }

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
