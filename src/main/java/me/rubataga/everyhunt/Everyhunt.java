package me.rubataga.everyhunt;

import de.themoep.inventorygui.DynamicGuiElement;
import me.rubataga.everyhunt.commands.CommandConfiguration;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.listeners.PortalListener;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.services.CompassRunnable;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.GameRules;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sound.midi.Track;

/**
 * Plugin class
 */
public final class Everyhunt extends JavaPlugin {

    private static Everyhunt pluginInstance;
    private static GameRules gameRules;

    public static Everyhunt getInstance(){
        return pluginInstance;
    }
    public static GameRules getGameRules() { return gameRules; }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        CommandConfiguration.register();
        pluginInstance = this;
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PortalListener(), this);
        pluginManager.registerEvents(new CompassListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CompassRunnable.compassRepeatingTask,0L,10L);

        this.saveDefaultConfig();
        gameRules = new GameRules();
        gameRules.setConfig(getConfig());
        gameRules.loadConfig();

        System.out.println("§bRubataga's Everyhunt plugin enabled!");
        Debugger.send("§aDebug mode for Everyhunt is enabled!");
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
