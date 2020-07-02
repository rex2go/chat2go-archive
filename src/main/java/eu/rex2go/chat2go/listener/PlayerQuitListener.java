package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.exception.AntiSpamException;
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

        if(user != null) {
            for(ChatUser users : plugin.getUserManager().getChatUsers()) {
                if(users.getLastChatter() == null) continue;
                if(users.getLastChatter().getUuid().equals(user.getUuid())) {
                    users.setLastChatter(null);
                }
            }

            plugin.getUserManager().getChatUsers().remove(user);

            if(mainConfig.isHideJoinMessage()) {
                event.setQuitMessage(null);
            } else if (mainConfig.isCustomLeaveMessageEnabled()) {
                try {
                    event.setQuitMessage(plugin.getChatManager().format(
                            user, "", false, mainConfig.getCustomLeaveMessage()));
                } catch (BadWordException | AntiSpamException ignored) {
                }
            }
        }
    }
}
