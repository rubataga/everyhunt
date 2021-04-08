package me.rubataga.everyhunt.events;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HunterCompassLocationUpdateEvent extends Event {

    private final Hunter hunter;
    private final Location location;

    public HunterCompassLocationUpdateEvent(Hunter hunter, Location location){
        this.hunter = hunter;
        this.location = location;
    }

    public Hunter getHunter(){
        return hunter;
    }

    public Location getLocation(){
        return location;
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
