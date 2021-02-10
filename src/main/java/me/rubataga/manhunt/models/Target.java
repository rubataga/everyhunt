package me.rubataga.manhunt.models;

import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.LinkedList;

public class Target extends GameEntity {

    Collection<Hunter> hunters = new LinkedList<>();

    public Target(Entity target){
        super(target);
    }

    public void addHunter(Hunter hunter){
        hunters.add(hunter);
    }

    public void removeHunter(Hunter hunter) { hunters.remove(hunter);}

    public Collection<Hunter> getHunters(){
        return hunters;
    }

}
