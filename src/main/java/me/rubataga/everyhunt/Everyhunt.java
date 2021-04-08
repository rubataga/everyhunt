package me.rubataga.everyhunt;

import me.rubataga.everyhunt.configs.CommandCfg;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.configs.PluginCfg;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.managers.GameManager;
import me.rubataga.everyhunt.managers.LobbyManager;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin class
 */
public final class Everyhunt extends JavaPlugin {

    private static Everyhunt pluginInstance;
    private static PluginManager pluginManager;

    public static Everyhunt getInstance(){
        return pluginInstance;
    }
    public static PluginManager getPluginManager(){
        return pluginManager;
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        pluginInstance = this;
        pluginManager = getServer().getPluginManager();

        Debugger.enabled = getConfig().getBoolean("debugMode");

        PluginCfg.initialize();
        GameCfg.initialize();
        CommandCfg.register();
        LobbyManager.initialize();
        GameManager.initialize();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, TrackingCompassRunnable.compassRepeatingTask,0L,10L);
        System.out.println("§bRubataga's Everyhunt plugin enabled!");
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true);
    }

    @Override
    public void onDisable() {
        System.out.println("§bRubataga's Everyhunt plugin disabled!");
    }

}