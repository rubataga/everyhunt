package me.rubataga.everyhunt;

import de.themoep.inventorygui.DynamicGuiElement;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sound.midi.Track;

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
                //System.out.println(hunter.getEntity().getName() + "'s compass null: " + (hunter.getCompass()==null));
                // if hunter is tracking death or portal, don't set compass. compass will continue to track the target's last location.
                if(hunter.getCompass() == null || (!hunter.isLodestoneTracking() && (hunter.isTrackingDeath() || hunter.isTrackingPortal()))){
                    continue;
                }
                Target target = hunter.getTarget();
                // lodestone tracking logic used when the hunter is in the nether or the end
                // lodestone tracking requires interval CompassMeta refreshes
                if(hunter.isLodestoneTracking()){
                    ItemStack compass;
                    if(TrackingCompassUtils.hasTrackingCompass(hunter)){
                        compass = hunter.getCompass();
                    } else {
                        compass = hunter.getLodestoneCompass();
                    }
//                    if(!hunter.inventoryHasCompass()){
//                        return;
//                    }
                    CompassMeta meta = ((CompassMeta)(compass.getItemMeta()));
                    meta.setLodestoneTracked(false);
                    // if tracking a portal or a death location (which don't change), track lastTracked
                    if (hunter.isTrackingPortal() || hunter.isTrackingDeath()) {
                        meta.setLodestone(hunter.getLastTracked());
                    } else {
                        meta.setLodestone(target.getEntity().getLocation());
                    }
                    compass.setItemMeta(meta);
                    hunter.getGUI().draw();
                    //((DynamicGuiElement)hunter.getGUI().getElement('c')).queryElement(hunter.getEntity());
                } else {
                    hunter.getEntity().setCompassTarget(target.getEntity().getLocation());
                }
            }
        }
    };

}
