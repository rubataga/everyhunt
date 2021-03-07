package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.Everyhunt;
import me.rubataga.everyhunt.utils.Debugger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginCfg {

    private static final Everyhunt EVERYHUNT = Everyhunt.getInstance();
    private static final Yaml YAML = new Yaml();
    private static final InputStream BASE_CONFIG_STREAM = EVERYHUNT.getResource("config.yml");
    private static final Map<String, Object> BASE_VALUE_MAP = YAML.load(BASE_CONFIG_STREAM);
    private static InputStream configFileInputStream;
    private static final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private static final Map<String, Object> valueMap;
//
//    public static final String DEFAULT_CONFIG_NAME = EVERYHUNT.getConfig();
//    public static final String BASE_CONFIG = new YamlConfiguration();

    public static String defaultGamemode;
    public static String commandPrefix;
    public static List<String> disabledCommands;
    public static boolean debugMode;

    static{
        for(Field f : PluginCfg.class.getFields()){
            FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
        setConfigFileInputStream();
        valueMap = YAML.load(configFileInputStream);
        setFieldsToValues();
        Debugger.enabled = debugMode;
    }

    public static void setConfigFileInputStream() {
        File configFile = new File(EVERYHUNT.getDataFolder(),"config.yml");
        Debugger.send("Searching for config.yml in Everyhunt folder");
        try{
            configFileInputStream = configFile.toURI().toURL().openStream();
            Debugger.send("config.yml found in Everyhunt folder!");
        } catch (IOException e){
            configFileInputStream = EVERYHUNT.getResource("config.yml");
            Debugger.send("config.yml found in JAR!");
        }
    }

    public static void setFieldsToValues() {
        //Debugger.send("Setting static values!");
        for(String key : FIELDS.keySet()){
            //Debugger.send("Editing key " + key);
            Field f = FIELDS.get(key);
            if(f!=null) {
                //Debugger.send("Field name: " + f.getName());
                Object obj = valueMap.get(key);
                if (obj == null) {
                    obj = BASE_VALUE_MAP.get(key);
                    Debugger.send("Couldn't find value! Using base value: " + obj);
                }
                try {
                    f.set(PluginCfg.class, obj);
                    Debugger.send("Static set: " + key + " = " + obj);
                } catch (IllegalAccessException set) {
                    Debugger.send("ERROR! Failed to set key: " + key);
                }
            }
        }
        Debugger.send("Loaded config.yml!");
    }

}
