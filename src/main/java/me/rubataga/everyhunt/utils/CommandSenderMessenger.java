package me.rubataga.everyhunt.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class CommandSenderMessenger {

    private String youString;
    private String otherString;

    private final CommandSender SENDER;
    private final boolean SENDER_IS_NULL;

    public CommandSenderMessenger(CommandSender cs, String youString, String otherString){
        this.SENDER = cs;
        this.SENDER_IS_NULL = (cs==null);
        this.youString = youString;
        this.otherString = otherString;
    }

    public CommandSenderMessenger(CommandSender cs){
        this.SENDER = cs;
        this.SENDER_IS_NULL = (cs==null);
        this.youString = "You are";
        this.otherString = " is";
    }

    public String getYouString(){
        return youString;
    }

    public String getOtherString(){
        return otherString;
    }

    public void setYouString(String str){
        this.youString = str;
    }

    public void setOtherString(String str){
        this.otherString = str;
    }

    public void message(String s){
        if(!SENDER_IS_NULL){
            SENDER.sendMessage(s);
        }
    }

    public void povMessage(Entity entity, String s, boolean sendToEntity, boolean sendToSender){
        StringBuilder sb = new StringBuilder(s);
            if(sendToEntity) {
                StringBuilder entitySb = new StringBuilder(sb);
                entitySb.insert(0, youString);
                entity.sendMessage(entitySb.toString());
            }
            if(sendToSender && !SENDER_IS_NULL){
                sb.insert(0,otherString).insert(0, entity.getName());
                SENDER.sendMessage(sb.toString());
            }
    }

    public void povMessage(Entity entity, String s){
        povMessage(entity,s,true,SENDER!=entity);
    }

    public void povMessageSenderOnly(Entity entity, String s){
        povMessage(entity,s,SENDER==entity, SENDER!=entity);
    }

}
