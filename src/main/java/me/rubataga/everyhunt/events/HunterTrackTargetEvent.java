package me.rubataga.everyhunt.events;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HunterTrackTargetEvent extends Event {

    private final Hunter hunter;
    private final Target target;

    public HunterTrackTargetEvent(Hunter hunter, Target target){
        this.hunter = hunter;
        this.target = target;
    }

    public Hunter getHunter(){
        return hunter;
    }

    public Target getTarget(){
        return target;
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
