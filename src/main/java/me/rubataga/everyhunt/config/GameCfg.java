package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class GameCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final Yaml YAML = new Yaml();
    private static final String BASE_GAMEMODE_NAME = "base.yml";
    private static final InputStream BASE_GAMEMODE_STREAM = EVERYHUNT.getResource(BASE_GAMEMODE_NAME);
    private static final String DEFAULT_GAMEMODE_NAME;
//    private static final InputStream DEFAULT_GAMEMODE_STREAM;
    private static final Map<String,Field> FIELDS = new LinkedHashMap<>();
    private static final Map<String, Object> BASE_VALUE_MAP = YAML.load(BASE_GAMEMODE_STREAM);
    private static final ConfigGui GUI = new ConfigGui();

    private static InputStream gamemodeInputStream;
    private static String loadedFileName = "";
    private static Map<String,Object> valueMap = new LinkedHashMap<>();
    private static Map<String, Object> defaultValueMap = new LinkedHashMap<>();

    public static boolean debugMode;
    public static String gameName;
    public static String baseGame;
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
//        Debugger.send("BASE_GAMEMODE_STREAM is null: " + (BASE_GAMEMODE_STREAM==null));
//        // adds this class' public fields to FIELDS
        for(Field f : GameCfg.class.getFields()){
            FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
        String defaultName =  EVERYHUNT.getConfig().getString("defaultGamemode");
        if(defaultName==null){
            defaultName = "base.yml";
        }
        Debugger.send("Default gamemode name: " + defaultName);
        DEFAULT_GAMEMODE_NAME = defaultName;
//        InputStream defaultStream = getInputStream(DEFAULT_GAMEMODE_NAME);
//        if(defaultStream==null){
//            defaultStream = BASE_GAMEMODE_STREAM;
//        }
//        DEFAULT_GAMEMODE_STREAM = defaultStream;
    }

    // load the default gamemode from config.yml
    public static void initialize(){
        loadGamemode(DEFAULT_GAMEMODE_NAME);
        defaultValueMap = valueMap;
        //loadGamemode("pog.yml");
//        defaultValueMap = valueMap;
    }
//
//    // loads a gamemode from an InputStream
//    public static void loadGamemode(InputStream inputStream){
////        Debugger.send("inputStream is null: " + (inputStream==null));
////        Debugger.send("inputStream is base: " + (inputStream==BASE_GAMEMODE_STREAM));
////        Debugger.send("Initial gamemode file: " + loadedFileName);
//        setInputStream(inputStream);
////        Debugger.send("Loaded gamemode file: " + loadedFileName);
//        setValuesToInputStream();
//        setFieldsToValues();
//    }

    public static void loadGamemode(String fileName){
        if(loadedFileName.equalsIgnoreCase(fileName)){
            return;
        }
        setInputStream(getInputStream(fileName));
        setValuesToInputStream();
        setFieldsToValues();
    }

    // sets gamemodeInputStream to an InputStream
    public static void setInputStream(InputStream inputStream) {
        gamemodeInputStream = inputStream;
    }
//
//    public static InputStream getBaseGamemodeStreamStream(){
//        return BASE_GAMEMODE_STREAM;
//    }

    // gets the InputStream for fileName.yml
    public static InputStream getInputStream(String fileName){
//        if(fileName==null){
//            if(DEFAULT_GAMEMODE_NAME!=null){
//                Debugger.send("Using default InputStream for file: " + DEFAULT_GAMEMODE_NAME);
//                return getInputStream(DEFAULT_GAMEMODE_NAME);
//            } else {
//                Debugger.send("1Config.yml's defaultGamemode is null! Using base values");
//                loadedFileName = "base.yml";
//                return BASE_GAMEMODE_STREAM;
//            }
//        }
//        if(fileName.equalsIgnoreCase(loadedFileName)){
//            return gamemodeInputStream;
//        }
//        if(fileName.equalsIgnoreCase("base.yml")){
//            Debugger.send("2Config.yml's defaultGamemode is null! Using base values");
//            loadedFileName = "base.yml";
//            return BASE_GAMEMODE_STREAM;
//        }
        InputStream inputStream;
        if(fileName.equalsIgnoreCase("base.yml")){
            Debugger.send(("Using base.yml"));
            inputStream = EVERYHUNT.getResource(BASE_GAMEMODE_NAME);
        } else {
            File configFile = new File(EVERYHUNT.getDataFolder(),fileName);
            Debugger.send("Searching for file " + configFile.getPath());
            if(configFile.exists()){
                try{
                    inputStream = configFile.toURI().toURL().openStream();
                    Debugger.send("Static input stream: " + inputStream);
                    Debugger.send("Using " + fileName);
                } catch (IOException e){
                    Debugger.send("ERROR! Could not get InputStream for file: " + fileName);
                }
            }
            if(fileName.equalsIgnoreCase(DEFAULT_GAMEMODE_NAME)){
                inputStream = getInputStream("base.yml");
            } else {
                Debugger.send("Recursive searching for default, file " + DEFAULT_GAMEMODE_NAME);
                inputStream = getInputStream(DEFAULT_GAMEMODE_NAME);
            }
        }
        loadedFileName = fileName;
        return inputStream;
//        if(fileName.equalsIgnoreCase(DEFAULT_GAMEMODE_NAME)){
//            Debugger.send("3Config.yml's defaultGamemode is null! Using base values");
//            loadedFileName = "base.yml";
//            return BASE_GAMEMODE_STREAM;
//        }
//        Debugger.send("Using default InputStream for file: " + DEFAULT_GAMEMODE_NAME);
//        return getInputStream(DEFAULT_GAMEMODE_NAME);
    }

    // sets valueMap to the values from gamemodeInputStream
    public static void setValuesToInputStream() {
//        Debugger.send("gamemodeInputStream equals BASE_GAMEMODE_STREAM: " + (gamemodeInputStream==BASE_GAMEMODE_STREAM));
        Debugger.send("gamemodeInputStream is null: " + (gamemodeInputStream==null));
        Debugger.send("Initial values map: " + valueMap);
        valueMap = YAML.load(gamemodeInputStream);
        Debugger.send("Final values map: " + valueMap);
    }
    
    public static Map<String,Field> getFields(){
        return FIELDS;
    }
    
    // sets GameCfg's public fields to valueMap;
    public static void setFieldsToValues() {
        //Debugger.send("Setting static values!");
        for(String key : FIELDS.keySet()){
            //Debugger.send("Editing key " + key);
            Field f = FIELDS.get(key);
            if(f!=null){
                //Debugger.send("Field name: " + f.getName());
                Object obj = valueMap.get(key);
                if(obj==null){
                    Debugger.send("Could find value! Using default value: " + obj);
                    obj = defaultValueMap.get(key);
                    if(obj==null){
                        obj = BASE_VALUE_MAP.get(key);
                        Debugger.send("Couldn't find value! Using base value: " + obj);
                    }
                }
                try {
                    f.set(GameCfg.class, obj);
                    Debugger.send("Static set: " + key + " = " + obj);
                } catch(IllegalAccessException set) {
                    Debugger.send("ERROR! Failed to set key: " + key);
                }
            }
        }
        Debugger.send("Loaded gamemode " + gameName);
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
