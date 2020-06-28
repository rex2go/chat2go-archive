package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends AbstractListener {

    public PlayerChatListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatUser chatUser = plugin.getUserManager().getUser(player);
        String message = event.getMessage();

        if(chatUser == null) {
            return;
        }

        try {
            event.setFormat(plugin.getChatManager().format(chatUser, message));
        } catch (BadWordException e) {
            event.setCancelled(true);

            // TODO message
        }
    }
}
