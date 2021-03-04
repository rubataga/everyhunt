package me.rubataga.everyhunt.services;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class CommandSenderMessenger {

    private String youString;
    private String otherString;

    private final CommandSender SENDER;
    private final boolean IS_NULL;

    public CommandSenderMessenger(CommandSender cs, String youString, String otherString){
        this.SENDER = cs;
        this.IS_NULL = (cs==null);
        this.youString = youString;
        this.otherString = otherString;
    }

    public CommandSenderMessenger(CommandSender cs){
        this.SENDER = cs;
        this.IS_NULL = (cs==null);
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

    public void msg(String s){
        if(!IS_NULL){
            SENDER.sendMessage(s);
        }
    }

    public void povMsg(Entity entity, String s, boolean sendToEntity, boolean sendToSender){
        StringBuilder sb = new StringBuilder(s);
        if(!IS_NULL){
            if(sendToEntity) {
                StringBuilder entitySb = new StringBuilder(sb);
                entitySb.insert(0, youString);
                entity.sendMessage(entitySb.toString());
            }
            if(sendToSender){
                sb.insert(0,otherString).insert(0, entity.getName());
                SENDER.sendMessage(sb.toString());
            }
        }
    }

    public void povMsg(Entity entity, String s){
        povMsg(entity,s,true,SENDER!=entity);
    }

    public void povMsgSenderOnly(Entity entity, String s){
        povMsg(entity,s,SENDER==entity, SENDER!=entity);
    }

}
