package me.rubataga.manhunt;

import dev.jorel.commandapi.CommandAPI;
import me.rubataga.manhunt.commands.CommandManager;
import me.rubataga.manhunt.services.CompassListener;
import me.rubataga.manhunt.services.CompassRepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin class
 */

public final class Manhunt extends JavaPlugin {

    // AP CSA: Method overriding
    // AP CSA: this keyword
    // AP CSA: void methods
    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        CommandManager.register();
        getServer().getPluginManager().registerEvents(new CompassListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CompassRepeatingTask.compassRepeatingTask,0L,10L);
        System.out.println("§bManhunt plugin enabled!");
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
