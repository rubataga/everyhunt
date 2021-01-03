package me.rubataga.manhunt;

import dev.jorel.commandapi.CommandAPI;
import me.rubataga.manhunt.services.CompassListener;
import me.rubataga.manhunt.models.CompassRepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public final class Manhunt extends JavaPlugin {

    public static List<Player> runners = new LinkedList<>();
    //public static HashMap<Player, Hunter> hunters = new HashMap<>();
    //public static HashMap<Player, Location> runnerLocations = new HashMap<>();

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        Commands.register();
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
