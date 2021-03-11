package me.rubataga.everyhunt.config;

import org.bukkit.plugin.java.JavaPlugin;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmbeddedYamlEditor {

    // constants
    private static final Yaml YAML = new Yaml();
    private final JavaPlugin PLUGIN;
    private final Class OWNER_CLASS;
    private final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private final String DEFAULT_RESOURCE_NAME;
    private final String EMBEDDED_RESOURCE_NAME;

    // variables
    public String loadedResourceName = "";
    public Map<String, Object> embeddedValueMap;
    public Map<String, Object> defaultValueMap;
    public Map<String, Object> loadedValueMap;

    // settings
    private boolean loadDefaultBeforeEmbedded = true;
    private String valueFormat = "%s: %s";

    public EmbeddedYamlEditor(Class ownerClass, JavaPlugin plugin, String DEFAULT_RESOURCE_NAME, String EMBEDDED_RESOURCE_NAME, String fileName) {
        this.PLUGIN = plugin;
        this.OWNER_CLASS = ownerClass;
        for(Field f : ownerClass.getFields()){
            this.FIELDS.put(f.getName(),f);
        }
        this.DEFAULT_RESOURCE_NAME = DEFAULT_RESOURCE_NAME;
        this.EMBEDDED_RESOURCE_NAME = EMBEDDED_RESOURCE_NAME;
        this.embeddedValueMap = YAML.load(getEmbeddedResource());
        load(fileName);
        defaultValueMap = loadedValueMap;
    }

    public EmbeddedYamlEditor(Class ownerClass, JavaPlugin plugin, String DEFAULT_RESOURCE_NAME, String EMBEDDED_RESOURCE_NAME) {
        this.PLUGIN = plugin;
        this.OWNER_CLASS = ownerClass;
        for(Field f : ownerClass.getFields()){
            this.FIELDS.put(f.getName(),f);
        }
        this.DEFAULT_RESOURCE_NAME = DEFAULT_RESOURCE_NAME;
        this.EMBEDDED_RESOURCE_NAME = EMBEDDED_RESOURCE_NAME;
        this.embeddedValueMap = YAML.load(getEmbeddedResource());
        load(DEFAULT_RESOURCE_NAME);
        setFieldsToValues();
        defaultValueMap = loadedValueMap;
    }

    public Map<String,Field> getFields() {
        return FIELDS;
    }

    public String getLoadedResourceName() {
        return loadedResourceName;
    }

    public Map<String, Object> getEmbeddedValueMap() {
        return embeddedValueMap;
    }

    public Map<String, Object> getDefaultValueMap() {
        return defaultValueMap;
    }

    public Map<String, Object> getLoadedValueMap() {
        return loadedValueMap;
    }

    public void setLoadDefaultBeforeEmbedded(boolean b){
        loadDefaultBeforeEmbedded = b;
    }

    public void setValueFormat(String s){
        valueFormat = s;
    }

    public void load(String fileName) {
        if (loadedResourceName.equalsIgnoreCase(fileName)) {
            return;
        }
        loadedValueMap = YAML.load(getInputStream(fileName));
        loadedResourceName = fileName;
        setFieldsToValues();
    }

    public InputStream getEmbeddedResource() {
        return PLUGIN.getResource(EMBEDDED_RESOURCE_NAME);
    }

    public InputStream getInputStream(String fileName) {
        InputStream inputStream = null;
        if (fileName.equalsIgnoreCase("$embedded")) {
            inputStream = getEmbeddedResource();
        } else {
            File configFile = new File(PLUGIN.getDataFolder(), fileName);
            if (configFile.exists()) {
                try {
                    inputStream = configFile.toURI().toURL().openStream();
                } catch (IOException ignored) {
                }
            }
            if (inputStream == null) {
                if (fileName.equalsIgnoreCase(DEFAULT_RESOURCE_NAME)) {
                    inputStream = getInputStream("$embedded");
                } else {
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
                } catch(IllegalAccessException ignored) {
                }
            }
        }
    }

    public Object getSafeValueFromMap(String key){
        Object obj = loadedValueMap.get(key);
        if(obj==null){
            if(loadDefaultBeforeEmbedded){
                obj = defaultValueMap.get(key);
                if(obj==null){
                    obj = embeddedValueMap.get(key);
                }
            } else {
                obj = embeddedValueMap.get(key);
                if(obj==null){
                    obj = defaultValueMap.get(key);
                }
            }
        }
        return obj;
    }

    // returns the value of a GameCfg public field
    public Object getValue(String key) {
        Object value;
        if(FIELDS.containsKey(key)){
            Field f = FIELDS.get(key);
            try {
                value = f.get(OWNER_CLASS);
                return value;
            }
            catch(IllegalAccessException e){
            }
        }
        return getSafeValueFromMap(key);
    }

    // returns a formatted string of a value accessible by getValue()
    public String getFormattedValue(String key){
        return String.format(valueFormat,key,getValue(key));
    }


    public void test(){
        System.out.println("test");
    }

}
