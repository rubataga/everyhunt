package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.entity.Entity;

public class Rules {

    public static boolean isBlacklisted(Entity entity){
        String entityKey = entity.getType().getKey().getKey();
        if(GameCfg.useBlacklist){
            for(String key : GameCfg.compassBlacklist){
                Debugger.send("CHECKING BLACKLIST KEY: " + key);
                if(entityKey.equals(key)){
                    return true;
                }
            }
            return false;
        } else {
            for(String key : GameCfg.compassWhitelist){
                Debugger.send("CHECKING WHITELIST KEY: " + key);
                if(entityKey.equals(key)){
                    return false;
                }
            }
            return true;
        }
    }

}
