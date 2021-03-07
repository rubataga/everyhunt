package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.services.TargetManager;
import me.rubataga.everyhunt.services.TargetService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class Target extends EveryhuntEntity {

    private final List<Hunter> hunters = new LinkedList<>();
    private final Map<World,Location> lastLocations = new HashMap<>();
    private World deathWorld = null;
    private boolean isRunner;

    public Target(Entity target){
        super(target);
        if(target instanceof Player){
            TargetManager.addRunner(this);
            this.isRunner = true;
        }
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

    public List<Hunter> getHunters(){
        return hunters;
    }

    public void updateWorldLocation(World world, Location location){
        lastLocations.put(world,location);
    }

    public void updateLastLocation() {
        World world = getEntity().getWorld();
        Location currentLocation = getEntity().getLocation();
        lastLocations.put(world,currentLocation);
    }

    public Location getLastLocationWorld(World world){
        if(lastLocations.containsKey(world)){
            return lastLocations.getOrDefault(world,null);
        }
        return null;
    }

    public Map<World,Location> getLastLocations(){
        return lastLocations;
    }

    public void updateDeathWorld(){
        this.deathWorld = getEntity().getWorld();
    }

    public World getDeathWorld(){
        return deathWorld;
    }

}
