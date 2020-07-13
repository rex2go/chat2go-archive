package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.logging.Level;

public abstract class CustomConfig {

    protected Chat2Go plugin;
    @Getter
    private String fileName;
    @Getter
    private FileConfiguration config;
    @Getter
    private File file;
    @Getter
    private int version;

    CustomConfig(Chat2Go plugin, String fileName) {
        this(plugin, fileName, 0);
    }

    CustomConfig(Chat2Go plugin, String fileName, int version) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder() + File.separator + fileName);
        this.version = version;

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        int ver = config.getInt("version");
        if (version != 0 && version > ver) {
            File outdated = new File(file.getPath() + ".outdated");
            if (outdated.exists()) {
                outdated.delete();
            }

            file.renameTo(outdated);

            this.file = new File(plugin.getDataFolder() + File.separator + fileName);
            if (!file.exists()) {
                plugin.saveResource(fileName, false);
            }

            this.config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void load() {
        Field[] fields = getClass().getFields();

        for(Field field : fields) {
            if(!field.isAnnotationPresent(ConfigInfo.class)) continue;
            ConfigInfo configInfo = field.getAnnotation(ConfigInfo.class);
            Object object = getConfig().get(configInfo.path());

            try {
                field.set(this, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    };

    public void reload() {
        this.file = new File(plugin.getDataFolder() + File.separator + fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        plugin.getLogger().log(Level.INFO, "Saving " + fileName + "..");

        Field[] fields = getClass().getFields();

        for(Field field : fields) {
            if(!field.isAnnotationPresent(ConfigInfo.class)) continue;
            ConfigInfo configInfo = field.getAnnotation(ConfigInfo.class);
            if(!configInfo.save()) continue;

            try {
                getConfig().set(configInfo.path(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            config.save(file);
            plugin.getLogger().log(Level.INFO, "Saved " + fileName + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigInfo {
        String path();
        boolean save() default true;
    }
}
