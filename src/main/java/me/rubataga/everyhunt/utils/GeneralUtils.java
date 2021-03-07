package me.rubataga.everyhunt.utils;

import org.bukkit.Location;

public class GeneralUtils {

    public static String formatLocation(Location location){
        return "[" + location.getX() + "," + location.getY() + "," + location.getZ() + "]";
    }

    public static String formatBlockLocation(Location location){
        return "[" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "]";
    }

    public static double[] getLocationArray(Location location){
        double[] xyz = new double[3];
        xyz[0] = location.getX();
        xyz[1] = location.getY();
        xyz[2] = location.getZ();
        return xyz;
    }

}
