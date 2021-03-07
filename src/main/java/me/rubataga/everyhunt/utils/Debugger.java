package me.rubataga.everyhunt.utils;

public class Debugger {

    public static boolean enabled = true;

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
            System.out.println("§d{DEBUG} " + str);
        }
    }

    public static void send(String[] strings){
        if(enabled){
            for(String str : strings){
                System.out.println("§d{DEBUG} " + str);
            }
        }
    }

    public static void send(Object[] objects){
        if(enabled){
            for(Object obj : objects){
                System.out.println("§c{DEBUG} " + obj);
            }
        }
    }

}
