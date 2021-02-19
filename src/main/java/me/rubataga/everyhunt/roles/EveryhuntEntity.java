package me.rubataga.everyhunt.roles;

import org.bukkit.entity.Entity;

public class EveryhuntEntity {

    private final Entity entity;

    public EveryhuntEntity(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}