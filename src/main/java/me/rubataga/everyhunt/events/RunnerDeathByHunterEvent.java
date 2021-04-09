package me.rubataga.everyhunt.events;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import org.bukkit.event.HandlerList;

public class RunnerDeathByHunterEvent extends RunnerDeathEvent {

    private final Hunter hunter;

    public RunnerDeathByHunterEvent(Target runner, Hunter hunter){
        super(runner);
        this.hunter = hunter;
    }

    public Hunter getHunter() {
        return hunter;
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
