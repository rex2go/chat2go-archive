package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.Placeholder;
import eu.rex2go.chat2go.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends AbstractListener {

    public PlayerJoinListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = new User(player);

        plugin.getUserManager().getUsers().add(user);

        if (mainConfig.isHideJoinMessage()) {
            event.setJoinMessage(null);

        } else if (mainConfig.isCustomJoinMessageEnabled()) {
            Placeholder username = new Placeholder("username", user.getName(), true);
            Placeholder prefix = new Placeholder("prefix", user.getPrefix(), true);
            Placeholder suffix = new Placeholder("suffix", user.getSuffix(), true);

            String format = plugin.getChatManager().processPlaceholders(mainConfig.getCustomJoinMessage(), user,
                    username, prefix, suffix);
            format = ChatColor.translateAlternateColorCodes('&', format);
            format = Chat2Go.parseHexColor(format);

            if (mainConfig.isJsonElementsEnabled()) {
                BaseComponent[] baseComponents = plugin.getChatManager().processJSONMessage(format, user);

                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.spigot().sendMessage(baseComponents);
                }

                event.setJoinMessage(null);
            } else {
                event.setJoinMessage(format);
            }
        }
    }
}
