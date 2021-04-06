package me.rubataga.everyhunt.listeners;

import me.rubataga.everyhunt.GameEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AutoAddJoiningPlayerListener implements Listener {

    @EventHandler
    public void autoAddJoiningPlayer(PlayerJoinEvent e){
        GameEngine.gamePlayers.add(e.getPlayer());
    }

}
