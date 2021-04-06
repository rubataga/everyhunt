package me.rubataga.everyhunt.config;

public abstract class AbstractCfg {

    static Object getValue(String key, Class<?> cfgClass) {
        try {
            return cfgClass.getField(key).get(cfgClass);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
        return null;
    }

    static String getFormattedValue(String key, Class<?> cfgClass){
        return String.format("%s: %s",key,getValue(key, cfgClass));
    }
}
