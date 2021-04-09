package me.rubataga.everyhunt.engines;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public interface Engine extends Listener {

    List<Listener> listeners = new ArrayList<>();
    PluginManager PM = Bukkit.getPluginManager();

    void initialize();

    void setListeners();

    void setRoles();

    List<Listener> getListeners();

    void registerListeners(JavaPlugin plugin);

//    default void registerListeners(JavaPlugin plugin){
//        for(Listener listener : listeners){
//            Debugger.send("Registering listener: " + listener);
//            PM.registerEvents(listener,plugin);
//        }
//    }

}
