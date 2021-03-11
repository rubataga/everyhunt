package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.utils.Debugger;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private static YamlEditor editor;

    public static String defaultGamemode;
    public static String commandPrefix;
    public static List<String> disabledCommands;
    public static boolean debugMode;

    static{
        for(Field f : PluginCfg.class.getFields()){
            FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }

    }

    public static void initialize(){
        Debugger.send("Fields!: " + FIELDS);
        editor = new YamlEditor(PluginCfg.class,EVERYHUNT,"config.yml","config.yml");
        editor.setFieldsToValues();
        Debugger.enabled = debugMode;
    }

}
