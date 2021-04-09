package me.rubataga.everyhunt.events;

import me.rubataga.everyhunt.roles.Target;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RunnerDeathEvent extends Event {

    private final Target runner;

    public RunnerDeathEvent(Target runner){
        this.runner = runner;
    }

    public Target getRunner() {
        return runner;
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
