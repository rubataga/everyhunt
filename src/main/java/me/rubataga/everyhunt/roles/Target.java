package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.managers.TrackingManager;
import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class Target extends EveryhuntRole {

    private final List<Hunter> hunters = new LinkedList<>();
    private final Map<World,Location> lastWorldLocations = new HashMap<>();
    private World deathWorld = null;

    public Target(Entity target){
        super(target);
        if(target instanceof Player && GameCfg.autoAddRunners){
            Player player = (Player) target;
            try{
                TrackingService.addRunner(player);
            } catch (EntityHasRoleException ignore){
            }
        }
    }

    public Target(Entity target, boolean addRunner){
        super(target);
        if(target instanceof Player && addRunner){
            Player player = (Player) target;
            try{
                TrackingService.addRunner(player);
            } catch (EntityHasRoleException ignore){
            }
        }
    }

    public void addHunter(Hunter hunter){
        hunters.add(hunter);
    }

    public void removeHunter(Hunter hunter) {
        hunters.remove(hunter);
        if(hunters.size()==0){
            TrackingManager.getTargets().remove(getEntity());
        }
    }

    public List<Hunter> getHunters(){
        return hunters;
    }

    public void updateLastWorldLocation(World world, Location location){
        lastWorldLocations.put(world,location);
    }

    public void updateLastLocation() {
        World world = getEntity().getWorld();
        Location currentLocation = getEntity().getLocation();
        Debugger.send("adding last location to world " + world + " : " + LocationUtils.formatBlockLocation(currentLocation));
        updateLastWorldLocation(world,currentLocation);
        Debugger.send("all locations: " + lastWorldLocations);
    }

    public Location getLastWorldLocation(World world){
        if(lastWorldLocations.containsKey(world)){
            return lastWorldLocations.getOrDefault(world,null);
        } else {
            return null;
        }
    }

    public Map<World,Location> getLastWorldLocations(){
        return lastWorldLocations;
    }

    public void updateDeathWorld(){
        this.deathWorld = getEntity().getWorld();
    }

    public World getDeathWorld(){
        return deathWorld;
    }

}
