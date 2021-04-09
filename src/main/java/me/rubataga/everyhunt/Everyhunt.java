package me.rubataga.everyhunt;

import me.rubataga.everyhunt.configs.CommandCfg;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.configs.PluginCfg;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.engines.ClassicEngine;
import me.rubataga.everyhunt.engines.Engine;
import me.rubataga.everyhunt.managers.GameManager;
import me.rubataga.everyhunt.managers.LobbyManager;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin class
 */
public final class Everyhunt extends JavaPlugin {

    private static Everyhunt pluginInstance;
    private static Engine pluginEngine;

    public static Everyhunt getInstance(){
        return pluginInstance;
    }
    public static Engine getPluginEngine() {
        return pluginEngine;
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        pluginInstance = this;
        File configDirectory = new File(this.getDataFolder(), "configs");
        configDirectory.mkdir();

        Debugger.enabled = getConfig().getBoolean("debugMode");

        PluginCfg.initialize();
        GameCfg.initialize();
        pluginEngine = GameCfg.getEngine();
        pluginEngine.initialize();
        LobbyManager.initialize();
        GameManager.initialize();
        CommandCfg.register();

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

    public static void setPluginEngine(Engine engine){
        pluginEngine = engine;
    }

}