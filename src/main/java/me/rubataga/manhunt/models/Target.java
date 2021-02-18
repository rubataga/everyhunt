package me.rubataga.manhunt.models;

import me.rubataga.manhunt.services.TargetManager;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.LinkedList;

public class Target extends ManhuntEntity {

    Collection<Hunter> hunters = new LinkedList<>();

    public Target(Entity target){
        super(target);
    }

    public void addHunter(Hunter hunter){
        hunters.add(hunter);
    }

    public void removeHunter(Hunter hunter) {
        hunters.remove(hunter);
        if(hunters.size()==0){
            TargetManager.getTargets().remove(getEntity());
        }
    }

    public Collection<Hunter> getHunters(){
        return hunters;
    }

}
