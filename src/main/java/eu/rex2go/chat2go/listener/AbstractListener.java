package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.config.ConfigManager;
import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

    protected Chat2Go plugin;
    protected ConfigManager configManager;

    public AbstractListener(Chat2Go plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
