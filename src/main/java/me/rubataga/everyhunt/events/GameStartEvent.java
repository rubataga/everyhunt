package me.rubataga.everyhunt.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private final String gameCfgName;

    public GameStartEvent(String gameCfgName){
        this.gameCfgName = gameCfgName;
    }

    public String getGameCfgName(){
        return gameCfgName;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
