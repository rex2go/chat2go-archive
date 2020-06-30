package eu.rex2go.chat2go.chat;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.config.BadWordConfig;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ChatManager {

    private Chat2Go plugin;
    private MainConfig mainConfig;

    @Getter
    private FilterMode filterMode;

    @Getter
    private BadWordConfig badWordConfig;

    public ChatManager(Chat2Go plugin) {
        this.plugin = plugin;
        this.mainConfig = Chat2Go.getMainConfig();
        this.filterMode = mainConfig.getChatFilterMode();

        badWordConfig = Chat2Go.getBadWordConfig();
    }

    public String format(ChatUser chatUser, String message) throws BadWordException {
        return format(chatUser, message, true, mainConfig.getChatFormat());
    }

    public String format(ChatUser chatUser, String message, boolean processMessage, String format) throws BadWordException {
        String username = chatUser.getName();
        String prefix = chatUser.getPrefix();
        String suffix = chatUser.getSuffix();

        format = ChatColor.translateAlternateColorCodes('&', format);
        format = plugin.parseHexColor(format);
        format = format.replaceAll(Pattern.quote(" +"), " ");
        message = processMessage(chatUser, message);

        format = format.replace("{prefix}",
                prefix.equals("") ? prefix : prefix + (mainConfig.isPrefixTrailingSpaceEnabled() ? " " : ""));
        format = format.replace("{username}", username);
        format = format.replace("{suffix}",
                suffix.equals("") ? suffix : (mainConfig.isSuffixLeadingSpaceEnabled() ? " " : "") + suffix);
        format = format.replace("{message}", message);

        return format.trim();
    }

    private String processMessage(ChatUser chatUser, String message) throws BadWordException {
        message = message.replace("%", "%%");

        if (mainConfig.isTranslateChatColorsEnabled()
                && chatUser.getPlayer().hasPermission(PermissionConstant.PERMISSION_CHAT_COLOR)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = plugin.parseHexColor(message);
        }

        if (!message.equals(filter(message))
                && !chatUser.getPlayer().hasPermission(PermissionConstant.PERMISSION_BAD_WORD_IGNORE)
                && mainConfig.isChatFilterEnabled()) {
            if (mainConfig.isBadWordNotificationEnabled()) {
                for (ChatUser staff : plugin.getUserManager().getChatUsers()) {
                    if (staff.getPlayer().hasPermission(PermissionConstant.PERMISSION_BAD_WORD_NOTIFY)
                            && chatUser.isBadWordNotificationEnabled()) {
                        staff.getPlayer().sendMessage(
                                Chat2Go.PREFIX + " " + Chat2Go.WARNING_PREFIX + " " + chatUser.getName() + ": " + ChatColor.RED + message);
                    }
                }
            }

            if (mainConfig.getChatFilterMode() == FilterMode.CENSOR) {
                message = filter(message);
            } else if (mainConfig.getChatFilterMode() == FilterMode.BLOCK) {
                throw new BadWordException(chatUser, message);
            }
        }


        return message;
    }

    private String filter(String message) {
        for (String badWord : getBadWords()) {
            if (message.toLowerCase().contains(badWord)) {
                for (String msg : message.split(" ")) {
                    if (msg.toLowerCase().contains(badWord)) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < msg.length(); i++) {
                            stringBuilder.append("*");
                        }

                        message = message.replaceAll(msg, stringBuilder.toString());
                    }
                }
            }
        }

        return message;
    }

    public String formatMsg(String from, String to, String message) {
        String format = mainConfig.getPrivateMessageFormat();

        format = ChatColor.translateAlternateColorCodes('&', format);

        format = format.replace("{from}", from);
        format = format.replace("{to}", to);
        format = format.replace("{message}", message);

        return format;
    }

    public ArrayList<String> getBadWords() {
        return getBadWordConfig().getBadWords();
    }
}
