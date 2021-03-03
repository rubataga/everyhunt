package me.rubataga.everyhunt.utils;

public class Debugger {

    public static boolean enabled = true;

    public static void enable(){
        Debugger.send("§aDebug mode for Everyhunt is enabled!");
        enabled = true;
    }

    public static void disable(){
        Debugger.send("§aDebug mode for Everyhunt is disabled!");
        enabled = false;
    }

    public static void send(String string){
        if(enabled){
            System.out.println("§c{DEBUG} " + string);
        }
    }

}
