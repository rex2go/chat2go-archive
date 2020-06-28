package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class BadWordConfigUtil {

    private FileConfiguration config;
    private File file;
    private Chat2Go plugin;

    public BadWordConfigUtil(Chat2Go plugin) {
        this.plugin = plugin;
        this.config = new YamlConfiguration();
        this.file = new File(plugin.getDataFolder() + File.separator + "badwords.yml");

        try {
            if (!file.exists()) {
                file.createNewFile();
                plugin.getLogger().log(Level.INFO, "badwords.yml created!");
            }
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        config.addDefault("badwords", new ArrayList<>());
        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
