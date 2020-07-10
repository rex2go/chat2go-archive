package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

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
            HashMap<String, String> placeholderMap = new HashMap<>();

            String username = user.getName();
            String prefix = user.getPrefix();
            String suffix = user.getSuffix();

            placeholderMap.put("username", username);
            placeholderMap.put("prefix", prefix);
            placeholderMap.put("suffix", suffix);

            String format = plugin.getChatManager().processPlaceholders(player, mainConfig.getCustomJoinMessage(), placeholderMap);

            event.setJoinMessage(format);
        }
    }
}
