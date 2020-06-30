package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.config.MainConfig;
import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

    protected Chat2Go plugin;
    MainConfig mainConfig;

    AbstractListener(Chat2Go plugin) {
        this.plugin = plugin;
        this.mainConfig = Chat2Go.getMainConfig();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
