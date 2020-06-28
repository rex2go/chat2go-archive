package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {

    public PlayerQuitListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ChatUser user = plugin.getUserManager().getUser(player);

        // TODO test
        if(user != null) plugin.getUserManager().getChatUsers().remove(user);

        if (configManager.isCustomLeaveMessageEnabled()) {
            try {
                event.setQuitMessage(plugin.getChatManager().format(
                        user, "", false, configManager.getCustomLeaveMessage()));
            } catch (BadWordException ignored) { }
        }
    }
}
