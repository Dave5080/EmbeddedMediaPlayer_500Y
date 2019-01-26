package embeddedmediaplayer;

import java.io.*;
import java.util.Properties;

@SuppressWarnings({"unused"})
public enum Configs {

    PATH("PATH", "images"),
    SCREEN("SCREENS", "0"),
    TIMER("TIMER", "30000"),
    VIDEONAME("VIDEONAME", "video.mp4");

    private static Properties config = null;
    private String key, defaultVal;

    Configs(String key, String defaultVal){
        this.key = key;
        this.defaultVal = defaultVal;
    }

    public String get(){
        return config.getProperty(key);
    }

    public String get(int i){
        return config.getProperty(String.format("%s%d", key, i));
    }

    public int getInt(){
        return Integer.parseInt(get());
    }

    public int getInt(int i){
        return Integer.parseInt(get(i));
    }

    public static void getConfig(){
        if (config != null)
            return;
        File tmpFile = new File("config.proprieties");
        config = new Properties();
        if(!tmpFile.exists())
            createConfig(config, tmpFile);
        else
            loadConfig(config, tmpFile);
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createConfig(Properties config, File tmpFile){
        try(OutputStream fileOutput = new FileOutputStream(tmpFile)) {
            tmpFile.createNewFile();
            for(Configs c : Configs.values())
                config.setProperty(c.key, c.defaultVal);
            config.store(fileOutput, null);
        } catch (IOException ignored) {}
    }

    private static void loadConfig(Properties config, File tmpFile){
        try(InputStream fileInput = new FileInputStream(tmpFile)){
            config.load(fileInput);
            for(Configs c : Configs.values())
                if(config.get(c.key) == null){
                    createConfig(config, tmpFile);
                    break;
                }
        } catch (IOException ignored) {}
    }
}
