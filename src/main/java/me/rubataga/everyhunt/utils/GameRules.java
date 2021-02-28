package me.rubataga.everyhunt.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.List;

public class GameRules {

    public FileConfiguration config;
    public static String GAMEMODE;
    public static boolean USE_BLACKLIST;
    public static String BLACKLIST_MESSAGE;
    public static List<String> COMPASS_BLACKLIST;
    public static List<String> COMPASS_WHITELIST;
    public static boolean RIGHT_CLICK_CYCLES_RUNNERS;

    public void loadConfig(){
        Debugger.enabled = config.getBoolean("debug");
        GAMEMODE = config.getString("gamemode");
        USE_BLACKLIST = config.getBoolean("useBlacklist");
        BLACKLIST_MESSAGE = config.getString("blacklistMessage");
        COMPASS_BLACKLIST = config.getStringList("compassBlacklist");
        COMPASS_WHITELIST = config.getStringList("compassWhitelist");
        RIGHT_CLICK_CYCLES_RUNNERS = config.getBoolean("rightClickCyclesRunners");
    }

    public void setConfig(FileConfiguration config){
        this.config = config;

    }

    public static boolean isBlacklisted(Entity entity){
        if(USE_BLACKLIST){
            for(String key : COMPASS_BLACKLIST){
                Debugger.send("CHECKING BLACKLIST KEY: " + key);
                if(entity.getType().getKey().getKey().equals(key)){
                    return true;
                }
            }
            return false;
        } else {
            for(String key : COMPASS_WHITELIST){
                Debugger.send("CHECKING WHITELIST KEY: " + key);
                if(entity.getType().getKey().getKey().equals(key)){
                    return false;
                }
            }
            return true;
        }
    }

}
