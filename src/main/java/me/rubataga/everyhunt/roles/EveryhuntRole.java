package me.rubataga.everyhunt.roles;

import me.rubataga.everyhunt.managers.TrackingManager;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class EveryhuntRole {

    private final Entity entity;

    public EveryhuntRole(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getName() {
        return entity.getName();
    }

    @Override
    public String toString() {
        return entity.getName();
    }

//    public boolean hasRole(RoleEnum role) {
//        return TrackingManager.getRoleMaps().get(role).containsKey(entity);
//    }
//
//    public List<RoleEnum> getRoles(){
//        List<RoleEnum> roles = new ArrayList<>();
//        for(RoleEnum role : TrackingManager.getRoleMaps().keySet()){
//            if(hasRole(role)){
//                roles.add(role);
//            }
//        }
//        return roles;
//    }
//
//    public EveryhuntRole getRoleObject(RoleEnum role){
//        return (EveryhuntRole) TrackingManager.getRoleMaps().get(role).getOrDefault(entity,null);
//    }
//
//    public List<EveryhuntRole> getEveryhuntRoles(){
//        List<EveryhuntRole> roles = new ArrayList<>();
//        for(RoleEnum role : getRoles()){
//            roles.add(getRoleObject(role));
//        }
//        return roles;
//    }


}