package me.rubataga.everyhunt.configs;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.utils.Debugger;
import me.rubataga.everyhunt.utils.EmbeddedYamlEditor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class PluginCfg {

    private static final Class<PluginCfg> PLUGIN_CFG_CLASS = PluginCfg.class;
    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static EmbeddedYamlEditor editor;

    public static boolean debugMode;
    public static String debugTag;

    public static String defaultGamemode;
    public static String commandPrefix;
    public static List<String> disabledCommands;

    public static void initialize(){
        editor = new EmbeddedYamlEditor(PLUGIN_CFG_CLASS,EVERYHUNT,"config.yml","config.yml");
//        Debugger.setToPluginCfg();
//        for(String key : getKeyFields().keySet()){
//            Debugger.send(getFormattedValue(key));
//        }
    }

    public static Object getValue(String key) {
        try {
            return PLUGIN_CFG_CLASS.getField(key).get(PLUGIN_CFG_CLASS);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
        return null;
    }

    public static String getFormattedValue(String key){
        return String.format("%s: %s",key,getValue(key));
    }

    public static Map<String, Field> getKeyFields(){
        return editor.getKeyFields();
    }


}
