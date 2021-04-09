package me.rubataga.everyhunt.engines;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.events.RunnerDeathByHunterEvent;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.ServerTrafficListener;
import me.rubataga.everyhunt.listeners.TeleportListener;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssassinEngine implements Engine {

    private final List<Listener> listeners = new ArrayList<>();

    public AssassinEngine(){
    }

    @Override
    public void initialize() {
        setListeners();
    }

    @Override
    public void setListeners() {
        listeners.add(new CompassListener());
        listeners.add(new DeathListener());
        listeners.add(new TeleportListener());
        listeners.add(new ServerTrafficListener());
    }

    @Override
    public List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public void registerListeners(JavaPlugin plugin){}

    @Override
    public void setRoles(){}

    @EventHandler
    private void onHunterKillTarget(RunnerDeathByHunterEvent e){
        Hunter hunter = e.getHunter();
        Target runner = e.getRunner();
        if(hunter.getTarget()==runner){

        }
    }

}
