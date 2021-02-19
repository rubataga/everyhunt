package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.services.TargetManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Target extends EveryhuntEntity {

    Collection<Hunter> hunters = new LinkedList<>();
    Map<World.Environment,Location> lastLocations = new HashMap<>();

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

    public void updateLastLocation() {
        World.Environment dimension = getEntity().getWorld().getEnvironment();
        Location currentLocation = getEntity().getLocation();
        lastLocations.put(dimension,currentLocation);
    }

    public Location getLastLocationDimension(World.Environment dimension){
        if(lastLocations.containsKey(dimension)){
            return lastLocations.get(dimension);
        }
        return null;
    }

}
