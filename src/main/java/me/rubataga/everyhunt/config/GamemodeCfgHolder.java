//package me.rubataga.everyhunt.config;
//
//import me.rubataga.everyhunt.Everyhunt;
//import me.rubataga.everyhunt.utils.Debugger;
//import org.yaml.snakeyaml.Yaml;
//import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
//
//import java.io.*;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Map;
//
//public class GamemodeCfgHolder {
//
//    private static final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(GamemodeCfgHolder.class.getClassLoader()));
//    private InputStream inputStream;
//
//    public void setInputStream(InputStream inputStream){
//        this.inputStream = inputStream;
//    }
//
//    public InputStream getInputStream() {
//        return inputStream;
//    }
//
//    public static InputStream getInputStream(String fileName) throws IOException {
//        File dataFolder = Everyhunt.getInstance().getDataFolder();
//        File configFile = new File(dataFolder,fileName);
//        return configFile.toURI().toURL().openStream();
//    }
//
//    public static GamemodeCfgHolder loadInputStream(GamemodeCfgHolder holder){
//        return yaml.loadAs(holder.getInputStream(), holder.getClass());
////        //Debugger.send("g instance gameName: " + gameName);
////        Debugger.send("GamemodeCfg gameName: " + g.gameName);
////        gcfg = g;
////        g.getClass().getFields();
//    }
//
//    public void injectGamemodeCfg() throws NoSuchFieldException, IllegalAccessException {
//        for(Field f: this.getClass().getFields()){
//            String fName = f.getName();
//            Debugger.send("Accessed holder field: " + fName);
//            Field injectF = GamemodeCfg.class.getField(fName);
//            Debugger.send("Accessed static field: " + injectF.getName());
//            injectF.set(f.getType(),f.get(this));
//            Debugger.send("New static value for " + injectF.getName() + ": " + injectF.get(GamemodeCfg.class));
//        }
//    }
//
//    public boolean debugMode;
//    public String gameName;
//    public String baseGame;
//    public List<String> gameDescription;
//
//    public String commandPrefix;
//    public List<String> disabledCommands;
//    public boolean useBlacklist;
//    public List<String> compassBlacklist;
//    public List<String> compassWhitelist;
//    public String blacklistMessage;
//
//    public boolean rightClickCyclesRunners;
//    public boolean rightClickResetsDeadCompass;
//
//    public boolean guiEnabled;
//    public boolean disablePortals;
//    public boolean huntersCanBeRunners;
//    public boolean nonRunnersCanBeTargeted;
//    public boolean autoAddRunners;
//
//}
