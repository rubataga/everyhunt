package me.rubataga.everyhunt.game;

import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TargetManager;
import org.bukkit.entity.Entity;

public class GameProgression {

    public static void anypercentWon(Target target){
        StringBuilder victoryTextBuilder = new StringBuilder("The Runners win!");
        if (target!=null){
            victoryTextBuilder.insert(0,target.getEntity().getName()).append(" has killed the Ender Dragon! ");
        }
        String victoryText = victoryTextBuilder.toString();
        for(Entity hunter : TargetManager.getHunters().keySet()){
            hunter.sendMessage(victoryText);
        }
    }

}
