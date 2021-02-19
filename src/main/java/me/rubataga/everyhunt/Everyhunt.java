package me.rubataga.everyhunt;

import me.rubataga.everyhunt.commands.CommandConfiguration;
import me.rubataga.everyhunt.listeners.CompassListener;
import me.rubataga.everyhunt.listeners.DeathListener;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.listeners.PortalListener;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.utils.TrackingCompassUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.CompassMeta;
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
        CommandAPI.onEnable(this);
        CommandConfiguration.register();
        getServer().getPluginManager().registerEvents(new PortalListener(), this);
        getServer().getPluginManager().registerEvents(new CompassListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, compassRepeatingTask,0L,10L);
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

    /**
     * Task that updates the compass location of a hunter with a {@link TrackingCompassUtils#trackingCompass()}
     */
    private static final Runnable compassRepeatingTask = ()-> {
        //for each hunter with a target
        for (Hunter hunter : TargetManager.getHunters().values()) {
            if (hunter != null && hunter.getTarget() != null) {
                // if hunter is tracking death or portal, don't set compass. compass will continue to track the target's last location.
                if(hunter.getCompass() == null || (!hunter.isLodestoneTracking() && (hunter.isTrackingDeath() || hunter.isTrackingPortal()))){
                    continue;
                }
                Target target = hunter.getTarget();
                // lodestone tracking logic used when the hunter is in the nether or the end
                if(hunter.isLodestoneTracking()){
                    if(!hunter.inventoryHasCompass()){
                        return;
                    }
                    CompassMeta meta = (CompassMeta)(hunter.getCompass().getItemMeta());
                    meta.setLodestoneTracked(false);
                    // if tracking a portal or a death location (which are static), track lastTracked
                    if (hunter.isTrackingPortal() || hunter.isTrackingDeath()) {
                        meta.setLodestone(hunter.getLastTracked());
                    } else {
                        meta.setLodestone(target.getEntity().getLocation());
                    }
                    hunter.getCompass().setItemMeta(meta);
                    hunter.getGUI().draw();
                } else {
                    hunter.getEntity().setCompassTarget(target.getEntity().getLocation());
                }
            }
        }
    };

}
