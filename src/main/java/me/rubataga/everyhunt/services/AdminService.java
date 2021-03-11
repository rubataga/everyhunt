package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.config.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.GeneralUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import java.util.Collection;
import java.util.List;

public class AdminService {

    public static void config(CommandSender sender){
        for(String key : GameCfg.getFields().keySet()){
            sender.sendMessage(GameCfg.getFormattedValue(key));
        }
    }

    public static void loadConfig(CommandSender sender, String fileName){
        sender.sendMessage("Attempting to load new gamemode: " + fileName);
        int i = fileName.indexOf(".yml");
        if(i==-1){
            fileName += ".yml";
        }
        GameCfg.load(fileName);
    }

    public static void sum(CommandSender sender, Collection<Entity> entities){
        for(Entity entity : entities){
            List<RoleEnum> roles = TargetManager.getRoles(entity);
            sender.sendMessage("§b" + entity.getName());
            sender.sendMessage("Roles: " + roles);
            if(roles.contains(RoleEnum.HUNTER)){
                Hunter hunter = TargetManager.getHunter(entity);
                sender.sendMessage("Compass is null?: " + (hunter.getCompass()==null));
                if(hunter.getTarget()!=null){
                    sender.sendMessage("Target: " + hunter.getTargetEntity().getName());
                }
                sender.sendMessage("Tracking death: " + hunter.isTrackingDeath());
                sender.sendMessage("Tracking portal: " + hunter.isTrackingPortal());
                sender.sendMessage("Lodestone tracking: " + hunter.isLodestoneTracking());
            }
            if(roles.contains(RoleEnum.TARGET)){
                Target target = TargetManager.getTarget(entity);
                sender.sendMessage("Hunters in pursuit: " + target.getHunters());
                for(World world : target.getLastLocations().keySet()){
                    sender.sendMessage("Last location in world " + world.getName() + ": " + GeneralUtils.formatBlockLocation(target.getLastLocationWorld(world)));
                }
            }
        }
    }

    public static void configGui(CommandSender sender){
        GameCfg.getGui().show((HumanEntity)sender);
    }

}
