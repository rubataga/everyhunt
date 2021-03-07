package me.rubataga.everyhunt;

import me.rubataga.everyhunt.config.CommandCfg;
import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.listeners.PortalListener;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.services.CompassRunnable;
import me.rubataga.everyhunt.utils.Debugger;
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
        CommandAPI.onEnable(this);
        pluginInstance = this;

        GameCfg.initialize();
        CommandCfg.register();
        Debugger.setToGameCfg();

        PluginManager pluginManager = getServer().getPluginManager();
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

    public void initializeConfigs(){
    }

    //        try {
//            GameCfg.setConfig(defaultGamemode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        GameCfg.loadConfig();
//
//        GamemodeCfgHolder holder;
//        try {
//            holder = new GamemodeCfgHolder();
//            holder.setInputStream(GamemodeCfgHolder.getInputStream(defaultGamemode));
//            GamemodeCfgHolder loadedHolder = GamemodeCfgHolder.loadInputStream(holder);
//            loadedHolder.injectGamemodeCfg();
//        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }

}