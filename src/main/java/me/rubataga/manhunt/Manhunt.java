package me.rubataga.manhunt;
import dev.jorel.commandapi.CommandAPI;
import me.rubataga.manhunt.events.CompassListener;
import me.rubataga.manhunt.models.Hunter;
import me.rubataga.manhunt.models.TrackingCompass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public final class Manhunt extends JavaPlugin {

    public static List<Player> runners = new LinkedList<>();
    public static HashMap<Player, Hunter> hunters = new HashMap<>();
    //public static HashMap<Player, Location> runnerLocations = new HashMap<>();

    @Override
    public void onEnable() {

        CommandAPI.onEnable(this);
        Commands.register();
        getServer().getPluginManager().registerEvents(new CompassListener(), this);
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

    public static boolean isRunner(Player runner){
        if(runners.contains(runner)){
            return true;
        }
        return false;
    }

    public static boolean isHunter(Player hunter){
        if(hunters.keySet().contains(hunter)){
            return true;
        }
        return false;
    }

    public static boolean isTrackerCompass(ItemStack compass){
        if(compass.getType()==Material.COMPASS){
            if(compass.getItemMeta().getDisplayName().contains("Tracker Comapss")){
                return true;
            }
        }
        return false;
    }

    public static Hunter getHunter(Player player){
        if(hunters.containsKey(player)){
            return hunters.get(player);
        }
        return null;
    }

    public static Collection<Hunter> getHunters(){
        return hunters.values();
    }

    public static List<Hunter> huntersTargeting(Player runner){
        return hunters.values().stream().filter(p -> p.getHunting()==runner).collect(Collectors.toList());
    }


    public static boolean giveCompass(Player hunter){
        if(isHunter(hunter)){
           if(hunter.getInventory().contains(Material.COMPASS)){
               List<ItemStack> compasses = Arrays.stream(hunter.getInventory().getContents()).filter(p -> p.getType()==Material.COMPASS).collect(Collectors.toList());
               for(ItemStack compass : compasses){
                   if(isTrackerCompass(compass)){
                       return false;
                   }
               }
            }
           hunter.getInventory().addItem(TrackingCompass.getTrackingCompass());
           return true;
        }
        return false;
    }

}
