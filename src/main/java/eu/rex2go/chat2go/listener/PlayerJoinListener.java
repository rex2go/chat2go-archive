package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.ChatUser;
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
        ChatUser user = new ChatUser(player);

        plugin.getUserManager().getChatUsers().add(user);

        if(mainConfig.isHideJoinMessage()) {
            event.setJoinMessage(null);
        } else if(mainConfig.isCustomJoinMessageEnabled()) {
            try {
                event.setJoinMessage(plugin.getChatManager().format(
                        user, "", false, mainConfig.getCustomJoinMessage()));
            } catch (BadWordException ignored) { }
        }
    }
}
