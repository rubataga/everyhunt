package me.rubataga.everyhunt.services;

import me.rubataga.everyhunt.game.GameCfg;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collection;

public class AdminService {

    public static void config(CommandSender sender){
        for(String key : GameCfg.PARAMETERS){
            Debugger.send("Checking " + key);
            if(key.equals(GameCfg.COMPASS_BLACKLIST) || key.equals(GameCfg.COMPASS_WHITELIST)){
                sender.sendMessage(GameCfg.formatEntityList(key));
            } else {
                sender.sendMessage(GameCfg.getFormattedValue(key));
            }
        }
    }

    public static void sum(CommandSender sender, Collection<Entity> entities){
        for(Entity entity : entities){
            if(TargetManager.getHunters().containsKey(entity)){
                Hunter hunter = TargetManager.getHunter(entity);
                sender.sendMessage("§b" + entity.getName());
                sender.sendMessage("Namespace key: " + entity.getType().getKey().getKey());
                sender.sendMessage("Type: " + entity.getType().name());
                sender.sendMessage("EntityType: " + EntityType.valueOf(entity.getType().name()));
                sender.sendMessage("Compass is null?: " + (hunter.getCompass()==null));
                sender.sendMessage("Tracked location: " + hunter.getEntity().getCompassTarget());
                if(hunter.getTarget()!=null){
                    sender.sendMessage("Target: " + hunter.getTargetEntity().getName());
                }
                sender.sendMessage("Tracking death: " + hunter.isTrackingDeath());
                sender.sendMessage("Tracking portal: " + hunter.isTrackingPortal());
                sender.sendMessage("Lodestone tracking: " + hunter.isLodestoneTracking());

            }
//            if(!TargetManager.getHunters().containsKey(entity) ||
//                    !TargetManager.getTargets().containsKey(entity) ||
//                    !TargetManager.getRunners().containsKey(entity)){
//                sender.sendMessage(entity.getName() + " had no roles.");
//            }
        }
    }

}
