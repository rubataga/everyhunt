package me.rubataga.everyhunt.config;

import me.rubataga.everyhunt.utils.Debugger;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlEditor {

    private static final Yaml YAML = new Yaml();

    private final JavaPlugin PLUGIN;
    private final Class OWNER_CLASS;
    private final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private final String DEFAULT_RESOURCE_NAME;
    private final String EMBEDDED_RESOURCE_NAME;
    public String loadedResourceName = "";

    public Map<String, Object> embeddedValueMap;
    public Map<String, Object> defaultValueMap;
    public Map<String, Object> loadedValueMap;

    public YamlEditor(Class ownerClass, JavaPlugin plugin, String DEFAULT_RESOURCE_NAME, String EMBEDDED_RESOURCE_NAME, String fileName) {
        this.PLUGIN = plugin;
        this.OWNER_CLASS = ownerClass;
        for(Field f : ownerClass.getFields()){
            this.FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
        this.DEFAULT_RESOURCE_NAME = DEFAULT_RESOURCE_NAME;
        this.EMBEDDED_RESOURCE_NAME = EMBEDDED_RESOURCE_NAME;
        this.embeddedValueMap = YAML.load(getEmbeddedResource());
        load(fileName);
        defaultValueMap = loadedValueMap;
    }

    public YamlEditor(Class ownerClass, JavaPlugin plugin, String DEFAULT_RESOURCE_NAME, String EMBEDDED_RESOURCE_NAME) {
        this.PLUGIN = plugin;
        this.OWNER_CLASS = ownerClass;
        for(Field f : ownerClass.getFields()){
            this.FIELDS.put(f.getName(),f);
            Debugger.send("Added field " + f.getName() + " to fields.");
        }
        this.DEFAULT_RESOURCE_NAME = DEFAULT_RESOURCE_NAME;
        this.EMBEDDED_RESOURCE_NAME = EMBEDDED_RESOURCE_NAME;
        this.embeddedValueMap = YAML.load(getEmbeddedResource());
        load(DEFAULT_RESOURCE_NAME);
        setFieldsToValues();
        defaultValueMap = loadedValueMap;
    }

    public void load(String fileName) {
        if (loadedResourceName.equalsIgnoreCase(fileName)) {
            return;
        }
        loadedValueMap = YAML.load(getInputStream(fileName));
        loadedResourceName = fileName;
    }

    public InputStream getEmbeddedResource() {
        return PLUGIN.getResource(EMBEDDED_RESOURCE_NAME);
    }

    public InputStream getInputStream(String fileName) {
        InputStream inputStream = null;
        if (fileName.equalsIgnoreCase("$embedded")) {
            Debugger.send(("Using embedded settings"));
            inputStream = getEmbeddedResource();
        } else {
            File configFile = new File(PLUGIN.getDataFolder(), fileName);
            Debugger.send("Searching for file " + configFile.getPath());
            if (configFile.exists()) {
                try {
                    inputStream = configFile.toURI().toURL().openStream();
                    Debugger.send("Static input stream: " + inputStream);
                    Debugger.send("Using " + fileName);
                } catch (IOException e) {
                    Debugger.send("ERROR! Could not get InputStream for file: " + fileName);
                }
            }
            if (inputStream == null) {
                if (fileName.equalsIgnoreCase(DEFAULT_RESOURCE_NAME)) {
                    inputStream = getInputStream("$embedded");
                } else {
                    Debugger.send("Recursive searching for default, file " + DEFAULT_RESOURCE_NAME);
                    inputStream = getInputStream(DEFAULT_RESOURCE_NAME);
                }
            }
        }
        loadedResourceName = fileName;
        return inputStream;
    }

    public void setFieldsToValues() {
        for(String key : FIELDS.keySet()){
            Field f = FIELDS.get(key);
            if(f!=null){
                Object obj = getSafeValueFromMap(key);
                try {
                    f.set(OWNER_CLASS, obj);
                    Debugger.send("Static set: " + key + " = " + obj);
                } catch(IllegalAccessException set) {
                    Debugger.send("ERROR! Failed to set key: " + key);
                }
            }
        }
    }

    public Object getSafeValueFromMap(String key){
        Object obj = loadedValueMap.get(key);
        if(obj==null){
            obj = embeddedValueMap.get(key);
            Debugger.send("Couldn't find value! Using base value: " + obj);
        }
        return obj;
    }

}
