package me.rubataga.everyhunt.exceptions;

import me.rubataga.everyhunt.roles.RoleEnum;
import org.bukkit.entity.Player;

public class PlayerHasRoleException extends Exception{

    public Player player;
    public RoleEnum role;
    public boolean has;

    public PlayerHasRoleException(Player player, RoleEnum role, boolean has){
        this.player = player;
        this.role = role;
        this.has = has;
    }

    public PlayerHasRoleException(Player player, RoleEnum role){
        this.player = player;
        this.role = role;
        this.has = true;
    }

    public PlayerHasRoleException(Player player, boolean has){
        this.player = player;
        this.role = null;
        this.has = has;
    }

    public PlayerHasRoleException(Player player){
        this.player = player;
        this.role = null;
        this.has = true;
    }

}
