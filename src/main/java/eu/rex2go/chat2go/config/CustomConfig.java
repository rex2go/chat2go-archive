package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class CustomConfig {

    @Getter
    private String fileName;
    @Getter
    private FileConfiguration config;
    protected Chat2Go plugin;
    @Getter
    private File file;

    public CustomConfig(Chat2Go plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        plugin.saveResource(fileName, false);

        this.file = new File(plugin.getDataFolder() + File.separator + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        plugin.getLogger().log(Level.INFO, "Saving " + fileName + "..");
        try {
            config.save(file);
            plugin.getLogger().log(Level.INFO, "Saved " + fileName + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
