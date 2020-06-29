package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends AbstractListener {

    public PlayerChatListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(!configManager.isChatEnabled()) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        ChatUser chatUser = plugin.getUserManager().getUser(player);
        String message = event.getMessage();

        if(chatUser == null) {
            return;
        }

        chatUser.setLastMessageTime(System.currentTimeMillis());
        chatUser.setLastMessage(message);

        try {
            event.setFormat(plugin.getChatManager().format(chatUser, message));
        } catch (BadWordException e) {
            event.setCancelled(true);

            player.sendMessage(ChatColor.RED + "Message could not be sent: Your message contains a blocked phrase.");
            // TODO add translation

            if(configManager.isBadWordNotificationEnabled()) {
                for (ChatUser staff : plugin.getUserManager().getChatUsers()) {
                    if (staff.getPlayer().hasPermission(PermissionConstant.PERMISSION_NOTIFY_BADWORD)
                            && chatUser.isBadWordNotificationEnabled()) {
                        staff.getPlayer().sendMessage(
                                Chat2Go.PREFIX + " " + Chat2Go.WARNING_PREFIX + " " + player.getName() + ": " + ChatColor.RED + message);
                    }
                }
            }
        }
    }
}
