package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

public class AdminService {

    public static void config(CommandSender sender){
        for(String key : GameCfg.getKeyFields().keySet()){
            sender.sendMessage(GameCfg.getFormattedValue(key));
        }
    }

    public static void loadConfig(CommandSender sender, String fileName){
        sender.sendMessage("Attempting to load new gamemode: " + fileName);
        int i = fileName.indexOf(".yml");
        if(i==-1){
            fileName += ".yml";
        }
        try{
            GameCfg.load(fileName);
            sender.sendMessage("Loaded gamemode " + fileName);
        } catch (FileNotFoundException e){
            sender.sendMessage(e.getMessage());
        }
    }

    public static void sum(CommandSender sender, Collection<Entity> entities){
        for(Entity entity : entities){
            List<RoleEnum> roles = TrackingManager.getRoles(entity);
            sender.sendMessage("§b" + entity.getName());
            sender.sendMessage("Roles: " + roles);
            if(roles.contains(RoleEnum.HUNTER)){
                Hunter hunter = TrackingManager.getHunter(entity);
                sender.sendMessage("Compass is null?: " + (hunter.getCompass()==null));
                if(hunter.getTarget()!=null){
                    sender.sendMessage("Target: " + hunter.getTargetEntity().getName());
                    if(hunter.getTarget().getLastLocations().containsKey(entity.getWorld())){
                        sender.sendMessage("Target last location in world " + entity.getWorld() + ": " + LocationUtils.formatBlockLocation(hunter.getTarget().getLastLocationWorld(entity.getWorld())));
                    }
                }
                sender.sendMessage("Tracking death: " + hunter.isTrackingDeath());
                sender.sendMessage("Tracking portal: " + hunter.isTrackingPortal());
                sender.sendMessage("Lodestone tracking: " + hunter.isLodestoneTracking());
                Location lastTracked = hunter.getLastTracked();
                if(lastTracked!=null){
                    sender.sendMessage("Last tracked world: " + lastTracked.getWorld());
                    sender.sendMessage("Last tracked coords: " + LocationUtils.formatBlockLocation(lastTracked));
                }
                if(hunter.isLodestoneTracking()){
                    Location location = ((CompassMeta)hunter.getCompass().getItemMeta()).getLodestone();
                    sender.sendMessage("Lodestone location: " + LocationUtils.formatBlockLocation(location));
                } else {
                    sender.sendMessage("Player compass location: " + LocationUtils.formatBlockLocation(((Player)entity).getCompassTarget()));
                }
            }
            if(roles.contains(RoleEnum.TARGET)){
                Target target = TrackingManager.getTarget(entity);
                sender.sendMessage("Hunters in pursuit: " + target.getHunters());
                for(World world : target.getLastLocations().keySet()){
                    sender.sendMessage("Last location in world " + world.getName() + ": " + LocationUtils.formatBlockLocation(target.getLastLocationWorld(world)));
                }
            }
        }
    }

    public static void configGui(CommandSender sender){
        GameCfg.getGui().show((HumanEntity)sender);
    }

}
