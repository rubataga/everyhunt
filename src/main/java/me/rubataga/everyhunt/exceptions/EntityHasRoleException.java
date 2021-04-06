package me.rubataga.everyhunt.exceptions;

import me.rubataga.everyhunt.roles.RoleEnum;
import org.bukkit.entity.Entity;

public class EntityHasRoleException extends Exception{

    public Entity entity;
    public RoleEnum role;
    public boolean has;

    public EntityHasRoleException(Entity entity, RoleEnum role, boolean has){
        super(constructString(entity,role,has));
        this.entity = entity;
        this.role = role;
        this.has = has;
    }

    public EntityHasRoleException(Entity entity, RoleEnum role){
        super(constructString(entity,role,true));
        this.entity = entity;
        this.role = role;
        this.has = true;
    }

    public EntityHasRoleException(Entity entity, boolean has){
        super(constructString(entity,null,has));
        this.entity = entity;
        this.role = null;
        this.has = has;
    }

    public EntityHasRoleException(Entity entity){
        super(constructString(entity,null,true));
        this.entity = entity;
        this.role = null;
        this.has = true;
    }

    private static String constructString(Entity entity, RoleEnum role, boolean has){
        if(has){
            return entity.getName() + " is a " + role;
        } else {
            return entity.getName() + " is not a " + role;
        }
    }

}
