package me.rubataga.manhunt;

import me.rubataga.manhunt.commands.CommandConfiguration;
import me.rubataga.manhunt.services.EventListener;
import me.rubataga.manhunt.services.CompassRepeatingTask;
import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin class
 */
public final class Manhunt extends JavaPlugin {

    private static Manhunt pluginInstance;

    public static Manhunt getInstance(){
        return pluginInstance;
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        CommandAPI.onEnable(this);
        CommandConfiguration.register();
        //CommandPanelAPIService.commandPanelsApi = CommandPanels.getAPI();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CompassRepeatingTask.compassRepeatingTask,0L,10L);
        System.out.println("§brubataga's Manhunt plugin enabled!");
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(true);
    }

    @Override
    public void onDisable() {
        System.out.println("§bManhunt plugin disabled!");
    }
}
