package me.rubataga.everyhunt.utils;

import me.rubataga.everyhunt.config.GameCfg;

public class Debugger {

    public static boolean enabled = false;
    private static String tag = "§d{DEBUG} ";

    public static void setToGameCfg(){
        setEnabledToGameCfg();
        setTagToGameCfg();
    }

    public static void setEnabledToGameCfg(){
        if(GameCfg.debugMode){
            enable();
        } else {
            disable();
        }
    }

    public static void setTagToGameCfg(){
        tag = GameCfg.debugTag;
    }

    public static void enable(){
        Debugger.send("Debug mode for Everyhunt is §aenabled!");
        enabled = true;
    }

    public static void disable(){
        Debugger.send("Debug mode for Everyhunt is §cdisabled!");
        enabled = false;
    }

    public static void send(String str){
        if(enabled){
            System.out.println(tag + str);
        }
    }

    public static void send(String[] strings){
        if(enabled){
            for(String str : strings){
                System.out.println(tag + str);
            }
        }
    }

    public static void send(Object[] objects){
        if(enabled){
            for(Object obj : objects){
                System.out.println(tag + obj);
            }
        }
    }

}
