package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerQuitListener extends AbstractListener {

    public PlayerQuitListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().getUser(player);

        if (user != null) {
            for (User users : plugin.getUserManager().getUsers()) {
                if (users.getLastChatter() == null) continue;
                if (users.getLastChatter().getUuid().equals(user.getUuid())) {
                    users.setLastChatter(null);
                }
            }

            plugin.getUserManager().getUsers().remove(user);

            if (mainConfig.isHideJoinMessage()) {
                event.setQuitMessage(null);
            } else if (mainConfig.isCustomLeaveMessageEnabled()) {
                HashMap<String, String> placeholderMap = new HashMap<>();

                String username = user.getName();
                String prefix = user.getPrefix();
                String suffix = user.getSuffix();

                placeholderMap.put("username", username);
                placeholderMap.put("prefix", prefix);
                placeholderMap.put("suffix", suffix);

                String format = plugin.getChatManager().processPlaceholders(player, mainConfig.getCustomJoinMessage(), placeholderMap);
                format = ChatColor.translateAlternateColorCodes('&', format);
                format = Chat2Go.parseHexColor(format);

                event.setQuitMessage(format);
            }
        }
    }
}
