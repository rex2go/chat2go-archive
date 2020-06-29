package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class BadWordConfigUtil {

    private FileConfiguration config;
    private Chat2Go plugin;

    public BadWordConfigUtil(Chat2Go plugin) {
        this.plugin = plugin;
        this.config = new YamlConfiguration();

        config.addDefault("badwords", new ArrayList<>());

        plugin.saveResource("badwords.yml", false);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        plugin.saveResource("badwords.yml", true);
    }
}
