package me.rubataga.everyhunt.exceptions;

import org.bukkit.entity.Player;

public class PlayerHasCompass extends Exception{

    public Player player;

    public PlayerHasCompass(Player player) {
        this.player = player;
    }

}
