package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.exception.AntiSpamException;
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
        ChatUser chatUser = plugin.getUserManager().getUser(player);

        if (chatUser == null) {
            return;
        }

        if (!mainConfig.isChatEnabled()) {
            event.setCancelled(true);
            chatUser.sendMessage("chat2go.chat.disabled", false);
            return;
        }

        String message = event.getMessage();
        long currentTime = System.currentTimeMillis();

        if (!player.hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_SLOW_MODE)
                && !player.hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_SLOWMODE)
                && mainConfig.isSlowModeEnabled()) {
            double cooldown =
                    MathUtil.round(
                            ((chatUser.getLastMessageTime() + mainConfig.getSlowModeSeconds() * 1000) - currentTime) / 1000F,
                            2);
            if (cooldown > 0) {
                chatUser.sendMessage("chat2go.chat.cooldown", false, String.valueOf(cooldown));
                event.setCancelled(true);
                return;
            }
        }

        try {
            event.setFormat(plugin.getChatManager().format(chatUser, message));
        } catch (BadWordException | AntiSpamException e) {
            event.setCancelled(true);
            chatUser.sendMessage(e.getMessage(), false);
        }
    }
}
