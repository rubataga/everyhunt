package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.yamleditor.YamlEditor;

import java.util.List;

public class PluginCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static YamlEditor editor;

    public static String defaultGamemode;
    public static String commandPrefix;
    public static List<String> disabledCommands;
    public static boolean debugMode;

    public static void initialize(){
        editor = new YamlEditor(PluginCfg.class,EVERYHUNT,"config.yml","config.yml");
        editor.setFieldsToValues();
        Debugger.enabled = debugMode;
    }

}
