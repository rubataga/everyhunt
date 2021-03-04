package me.rubataga.everyhunt.game;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.rubataga.everyhunt.guis.ConfigGui;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameCfg {

    private static FileConfiguration config;

    //PARAMETERS
    public static final String DEBUG = "debugMode";
    public static final String GAME_NAME = "name";
    public static final String COMMAND_PREFIX = "commandPrefix";
    public static final String DISABLED_COMMANDS = "disabledCommands";
    public static final String USE_BLACKLIST = "useBlacklist";
    public static final String COMPASS_BLACKLIST = "compassBlacklist";
    public static final String COMPASS_WHITELIST = "compassWhitelist";
    public static final String AUTO_ADD_RUNNERS = "autoAddRunners";
    public static final String RIGHT_CLICK_CYCLES_RUNNERS = "rightClickCyclesRunners";

    //VALUES
    public static boolean debugMode;
    public static String name;
    public static String commandPrefix;
    public static List<String> disabledCommands;
    public static boolean useBlacklist;
    public static List<String> compassBlacklist;
    public static List<String> compassWhitelist;
    public static boolean autoAddRunners;
    public static boolean rightClickCyclesRunners;

    private static final String VALUE_TEMPLATE = "%s: %s";
    public static final ImmutableList<String> PARAMETERS;
    private static final ImmutableMap<String, Object> defaultValues;
    private static final ConfigGui GUI;

    static {
        PARAMETERS = ImmutableList.copyOf(Arrays.asList(
                DEBUG,GAME_NAME,USE_BLACKLIST,
                COMMAND_PREFIX, DISABLED_COMMANDS,
                COMPASS_BLACKLIST, COMPASS_WHITELIST,
                AUTO_ADD_RUNNERS,RIGHT_CLICK_CYCLES_RUNNERS));

        defaultValues = ImmutableMap.copyOf(new HashMap<>() {{
            put(DEBUG,false);
            put(GAME_NAME,"default");
            put(COMMAND_PREFIX,"eh");
            put(DISABLED_COMMANDS,new ArrayList<>());
            put(USE_BLACKLIST,true);
            put(COMPASS_BLACKLIST,new ArrayList<>());
            put(COMPASS_WHITELIST,new ArrayList<>());
            put(AUTO_ADD_RUNNERS,true);
            put(RIGHT_CLICK_CYCLES_RUNNERS,false);
        }});

        GUI = new ConfigGui();
    }

    public static void setConfig(FileConfiguration cfg){
        config = cfg;
    }

    public static void loadConfig(){
        loadConfig(config);
    }

    public static void loadConfig(FileConfiguration cfg){
        config = cfg;
        Debugger.send("Loading config using : " + config.getString(GAME_NAME));
        debugMode = (boolean) getValue(DEBUG);
        if(debugMode){ Debugger.enable(); }
        name = (String) getValue(GAME_NAME);
        commandPrefix = (String) getValue(COMMAND_PREFIX);
        useBlacklist = (boolean) getValue(USE_BLACKLIST);
        autoAddRunners = (boolean) getValue(AUTO_ADD_RUNNERS);
        rightClickCyclesRunners = (boolean) getValue(RIGHT_CLICK_CYCLES_RUNNERS);

        // enabled commands
        disabledCommands = config.getStringList(DISABLED_COMMANDS);
        Debugger.send("disabled commands: " + disabledCommands);

        //compass blacklist
        if(config.contains(COMPASS_BLACKLIST)){
            compassBlacklist = config.getStringList(COMPASS_BLACKLIST);
        } else {
            compassBlacklist = new ArrayList<>();
        }
        if (config.contains(COMPASS_WHITELIST)) {
            compassWhitelist = config.getStringList(COMPASS_WHITELIST);
        } else {
            compassWhitelist = new ArrayList<>();
        }
        Debugger.send("blacklisted entities: " + compassBlacklist);
        Debugger.send("whitelisted entities: " + compassWhitelist);
    }

    public static Object getValue(String key){
        Object value;
        if(config.contains(key)){
            value = config.get(key);
        } else {
            value = defaultValues.get(key);
        }
        Debugger.send(key + " returning as " + value);
        return value;
    }
//
//    private static String getStringValue(String key){
//        String value;
//        if(config.contains(key)){
//            value = config.getString(key);
//        } else {
//            value = (String) defaultValues.get(key);
//        }
//        Debugger.send(key + " set to " + value);
//        return value;
//    }
//
//    private static boolean getBoolValue(String key){
//        boolean value;
//        if(config.contains(key)){
//            value = config.getBoolean(key);
//        } else {
//            value = (boolean) defaultValues.get(key);
//        }
//        Debugger.send(key + " set to " + value);
//        return value;
//    }

    public static String getFormattedValue(String key){
        switch (key) {
            case DEBUG: return String.format(VALUE_TEMPLATE, key, debugMode);
            case GAME_NAME: return String.format(VALUE_TEMPLATE, key, name);
            case COMMAND_PREFIX: return String.format(VALUE_TEMPLATE, key, commandPrefix);
            case DISABLED_COMMANDS: return String.format(VALUE_TEMPLATE, key, disabledCommands);
            case USE_BLACKLIST: return String.format(VALUE_TEMPLATE, key, useBlacklist);
            case COMPASS_BLACKLIST: return String.format(VALUE_TEMPLATE, key, compassBlacklist);
            case COMPASS_WHITELIST: return String.format(VALUE_TEMPLATE, key, compassWhitelist);
            case AUTO_ADD_RUNNERS: return String.format(VALUE_TEMPLATE, key, autoAddRunners);
            case RIGHT_CLICK_CYCLES_RUNNERS: return String.format(VALUE_TEMPLATE, key, rightClickCyclesRunners);
        }
        return String.format(VALUE_TEMPLATE, key, "ERROR");
    }

    public static String formatEntityList(String key){
        StringBuilder sb = new StringBuilder(key).append(": ");
        List<String> entityList;
        if(key.equals(COMPASS_BLACKLIST)){
            entityList = compassBlacklist;
        } else if (key.equals(COMPASS_WHITELIST)) {
            entityList = compassWhitelist;
        } else {
            return sb.append("empty!").toString();
        }
        if(entityList.size()==0){
            return sb.append("empty!").toString();
        }
        for(String entity : entityList){
            sb.append(entity).append(", ");
        }
        int i = sb.length();
        sb.delete(i-2,i).append(".");
        return sb.toString();
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
