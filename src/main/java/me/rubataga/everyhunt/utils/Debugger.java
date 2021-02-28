package me.rubataga.everyhunt.utils;

public class Debugger {

    public static boolean enabled = false;

    public static void send(String string){
        if(enabled){
            System.out.println("§c{DEBUG} " + string);
        }
    }

}
