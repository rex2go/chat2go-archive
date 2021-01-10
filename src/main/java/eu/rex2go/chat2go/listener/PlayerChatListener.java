package eu.rex2go.chat2go.listener;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.chat.exception.AntiSpamException;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.user.User;
import eu.rex2go.chat2go.util.MathUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.UnknownFormatConversionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PlayerChatListener extends AbstractListener {

    public PlayerChatListener(Chat2Go plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        User user = plugin.getUserManager().getUser(player);

        if (user == null) {
            return;
        }

        // Chat disabled
        if (!mainConfig.isChatEnabled()) {
            event.setCancelled(true);
            user.sendMessage("chat2go.chat.disabled", false);
            return;
        }

        String message = event.getMessage();
        long currentTime = System.currentTimeMillis();

        if (!user.hasPermission(PermissionConstants.PERMISSION_CHAT_BYPASS_SLOW_MODE)
                && mainConfig.isSlowModeEnabled()) {
            double cooldown =
                    MathUtil.round(
                            ((user.getLastMessageTime() + mainConfig.getSlowModeCooldown() * 1000) - currentTime) / 1000F,
                            2);

            if (cooldown > 0) {
                user.sendMessage("chat2go.chat.cooldown", false, String.valueOf(cooldown));

                event.setCancelled(true);

                return;
            }
        }

        try {
            if (mainConfig.isChatFormatEnabled()) {
                String format = plugin.getChatManager().format(message, user);
                event.setFormat(format);

                if ((mainConfig.isJsonElementsEnabled()
                        || mainConfig.isWorldChatEnabled()
                        || mainConfig.isRangeChatEnabled())
                        && !event.isCancelled()) {
                    event.setCancelled(true);

                    Collection<? extends Player> recipients = Bukkit.getOnlinePlayers();

                    if (mainConfig.isWorldChatEnabled()) {
                        recipients =
                                recipients.stream().filter(
                                        r -> r.getWorld().equals(player.getWorld())
                                ).collect(Collectors.toList());
                    }

                    if (mainConfig.isRangeChatEnabled()) {
                        recipients =
                                recipients.stream().filter(
                                        r -> r.getLocation().distance(player.getLocation()) <= mainConfig.getRangeChatRange()
                                ).collect(Collectors.toList());
                    }

                    StringBuilder logMessage = new StringBuilder();

                    if (mainConfig.isJsonElementsEnabled()) {
                        BaseComponent[] baseComponents = plugin.getChatManager().processJSONMessage(format, user);

                        for (Player all : recipients) {
                            all.spigot().sendMessage(baseComponents);
                        }

                        for (BaseComponent baseComponent : baseComponents) {
                            logMessage.append(baseComponent.toLegacyText());
                        }
                    } else {
                        for (Player all : recipients) {
                            all.sendMessage(format);
                        }

                        logMessage.append(format);
                    }

                    String logMessageStr = logMessage.toString().replaceAll("§.", "");
                    plugin.getLogger().log(Level.INFO, logMessageStr);
                }
            } else {
                event.setMessage(plugin.getChatManager().processMessage(message, user));
            }
        } catch (BadWordException | AntiSpamException e) {
            event.setCancelled(true);
            user.sendMessage(e.getMessage(), false);
        } catch (UnknownFormatConversionException e) {
            plugin.getLogger().log(Level.SEVERE, "Error in chat formatting. If you're using Placeholder API, check if" +
                    " all required extensions are installed.");
        }
    }
}
