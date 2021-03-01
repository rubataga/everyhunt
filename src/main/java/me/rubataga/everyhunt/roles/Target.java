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

    private final Collection<Hunter> hunters = new LinkedList<>();
    private final Map<World.Environment,Location> lastLocations = new HashMap<>();
    private World deathWorld = null;

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

    public void updateDimensionLocation(World.Environment dimension, Location location){
        lastLocations.put(dimension,location);
    }

    public void updateLastLocation() {
        World.Environment dimension = getEntity().getWorld().getEnvironment();
        Location currentLocation = getEntity().getLocation();
        lastLocations.put(dimension,currentLocation);
    }

    public Location getLastLocationDimension(World.Environment dimension){
        if(lastLocations.containsKey(dimension)){
            return lastLocations.getOrDefault(dimension,null);
        }
        return null;
    }

    public void updateDeathWorld(){
        this.deathWorld = getEntity().getWorld();
    }

    public World getDeathWorld(){
        return deathWorld;
    }

}
