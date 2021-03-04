package me.rubataga.everyhunt;

import me.rubataga.everyhunt.commands.CommandConfiguration;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.PortalListener;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.services.CompassRunnable;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.game.GameCfg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin class
 */
public final class Everyhunt extends JavaPlugin {

    private static Everyhunt pluginInstance;

    public static Everyhunt getInstance(){
        return pluginInstance;
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        PluginManager pluginManager = getServer().getPluginManager();

        this.saveDefaultConfig();
        GameCfg.setConfig(getConfig());
        GameCfg.loadConfig();

        CommandAPI.onEnable(this);
        CommandConfiguration.register();

        pluginManager.registerEvents(new PortalListener(), this);
        pluginManager.registerEvents(new CompassListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CompassRunnable.compassRepeatingTask,0L,10L);
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