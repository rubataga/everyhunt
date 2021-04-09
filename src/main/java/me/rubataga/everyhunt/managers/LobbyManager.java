package me.rubataga.everyhunt.managers;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.configs.GameCfg;
import me.rubataga.everyhunt.exceptions.EntityHasRoleException;
import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.roles.RoleEnum;
import me.rubataga.everyhunt.roles.Target;
import me.rubataga.everyhunt.services.TrackingService;
import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LobbyManager {

    private static final List<Player> lobbyPlayers = new ArrayList<>();
    public static boolean gameStarted;
    public static JavaPlugin plugin;
    public static PluginManager pluginManager = Bukkit.getPluginManager();

    public static void initialize(){
        plugin = Everyhunt.getInstance();
    }

    public static List<Player> getLobbyPlayers(){
        return lobbyPlayers;
    }



}
