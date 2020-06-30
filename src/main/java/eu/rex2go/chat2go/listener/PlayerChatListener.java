package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
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
        Player player = event.getPlayer();

        if (!configManager.isChatEnabled()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Chat is disabled.");
            // TODO customizable
            return;
        }

        ChatUser chatUser = plugin.getUserManager().getUser(player);
        String message = event.getMessage();
        long currentTime = System.currentTimeMillis();

        if (chatUser == null) {
            return;
        }

        if (!player.hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_SLOW_MODE)
                && configManager.isSlowModeEnabled()) {
            double cooldown =
                    MathUtil.round(
                            ((chatUser.getLastMessageTime() + configManager.getSlowModeSeconds() * 1000) - currentTime) / 1000F,
                            2);
            if (cooldown > 0) {
                player.sendMessage(ChatColor.RED + "Chat is in slow mode. Please wait " + cooldown + " seconds before" +
                        " sending a message.");
                // TODO customizable
                event.setCancelled(true);
                return;
            }
        }

        chatUser.setLastMessageTime(currentTime);
        chatUser.setLastMessage(message);

        try {
            event.setFormat(plugin.getChatManager().format(chatUser, message));
        } catch (BadWordException e) {
            event.setCancelled(true);

            player.sendMessage(ChatColor.RED + "Message could not be sent: Your message contains a blocked phrase.");
            // TODO customizable
        }
    }
}
