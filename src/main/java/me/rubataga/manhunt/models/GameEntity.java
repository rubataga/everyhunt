package me.rubataga.manhunt.models;

import org.bukkit.entity.Entity;

public class GameEntity {

    private final Entity entity;

    public GameEntity(Entity entity){
        this.entity = entity;
    }
    public Entity getEntity() {
        return entity;
    }

}
