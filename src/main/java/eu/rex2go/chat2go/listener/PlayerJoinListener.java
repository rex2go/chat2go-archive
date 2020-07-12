package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.Placeholder;
import eu.rex2go.chat2go.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends AbstractListener {

    public PlayerJoinListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler
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

            String format = plugin.getChatManager().processPlaceholders(user, mainConfig.getCustomJoinMessage(),
                    username, prefix, suffix);
            format = ChatColor.translateAlternateColorCodes('&', format);
            format = Chat2Go.parseHexColor(format);

            event.setJoinMessage(format);
        }
    }
}
