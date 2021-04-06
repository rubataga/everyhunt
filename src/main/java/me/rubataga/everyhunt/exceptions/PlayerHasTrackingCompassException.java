package me.rubataga.everyhunt.exceptions;

import org.bukkit.entity.Player;

public class PlayerHasTrackingCompassException extends Exception{

    public Player player;

    public PlayerHasTrackingCompassException(Player player) {
        super(player + " already has a tracking compass!");
        this.player = player;
    }

}
