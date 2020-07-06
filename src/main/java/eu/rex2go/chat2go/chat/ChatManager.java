package eu.rex2go.chat2go.chat;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.exception.AntiSpamException;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.config.BadWordConfig;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
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

    public String format(ChatUser chatUser, String message) throws BadWordException, AntiSpamException {
        return format(chatUser, message, true, mainConfig.getChatFormat());
    }

    public String format(ChatUser chatUser, String message, boolean processMessage, String format) throws BadWordException, AntiSpamException {
        String username = chatUser.getName();
        String prefix = chatUser.getPrefix();
        String suffix = chatUser.getSuffix();

        format = ChatColor.translateAlternateColorCodes('&', format);
        format = Chat2Go.parseHexColor(format);

        if (processMessage) {
            format = format.replaceAll(Pattern.quote(" +"), " ");
            message = processMessage(chatUser, message);
        }

        format = format.replace("{prefix}",
                prefix.equals("") ? prefix : prefix + (mainConfig.isPrefixTrailingSpaceEnabled() ? " " : ""));
        format = format.replace("{username}", username);
        format = format.replace("{suffix}",
                suffix.equals("") ? suffix : (mainConfig.isSuffixLeadingSpaceEnabled() ? " " : "") + suffix);
        format = format.replace("{message}", message);

        return format.trim();
    }

    private String processMessage(ChatUser chatUser, String message) throws BadWordException, AntiSpamException {
        message = message.replace("%", "%%");
        message = antiSpamCheck(chatUser, message);
        String[] ads = new String[0];

        if (!chatUser.getPlayer().hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_IP)) {
            ads = filterAdvertisement(message);
        }

        chatUser.setLastMessage(message);
        chatUser.setLastMessageTime(System.currentTimeMillis());

        if (mainConfig.isTranslateChatColorsEnabled()
                && chatUser.getPlayer().hasPermission(PermissionConstant.PERMISSION_CHAT_COLOR)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = Chat2Go.parseHexColor(message);
        }

        if (!message.equals(filter(message, ads))
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
                message = filter(message, ads);
            } else if (mainConfig.getChatFilterMode() == FilterMode.BLOCK) {
                throw new BadWordException(chatUser, message);
            }
        }


        return message;
    }

    private String filter(String message, String... additional) {
        List<String> badWords = (List<String>) getBadWords().clone();

        badWords.addAll(Arrays.asList(additional));

        for (String badWord : badWords) {
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

    private String[] filterAdvertisement(String message) {
        ArrayList<String> ads = new ArrayList<>();

        if (mainConfig.isLinkBlockEnabled()) {
            Pattern pattern = Pattern.compile("(\\d+\\s*\\d*\\.\\s*\\d+\\s*\\d*\\.\\s*\\d+\\s*\\d*\\" +
                    ".\\s*\\d+\\s*\\d*:?\\s*\\d*\\s*\\d*)");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String ip = matcher.group();

                if (Chat2Go.getLinkWhitelistConfig().getLinkWhitelist().stream().noneMatch(ip::equalsIgnoreCase)) {
                    ads.add(ip);
                }
            }

            pattern = Pattern.compile("(\\w*\\.[^0-9]{1,4}\\b)");
            matcher = pattern.matcher(message.toLowerCase());

            while (matcher.find()) {
                String link = matcher.group();

                if (Chat2Go.getLinkWhitelistConfig().getLinkWhitelist().stream().noneMatch(link::equalsIgnoreCase)) {
                    ads.add(link);
                }
            }
        }

        return ads.toArray(new String[0]);
    }

    private String antiSpamCheck(ChatUser user, String message) throws AntiSpamException {
        if (!mainConfig.isAntiSpamEnabled()) {
            return message;
        }

        if (user.getPlayer().hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_ANTI_SPAM)
                || user.getPlayer().hasPermission(PermissionConstant.PERMISSION_CHAT_BYPASS_ANTISPAM)) {
            return message;
        }

        double diff = ((System.currentTimeMillis() - user.getLastMessageTime()) / 1000D);

        if (diff < 0.5) {
            throw new AntiSpamException("chat2go.chat.antispam.too_fast");
        }

        if (diff < 30) {
            if (user.getLastMessage().equalsIgnoreCase(message)) {
                throw new AntiSpamException("chat2go.chat.antispam.repeating");
            }
        }

        if (message.replace(" ", "").length() > 3) {
            double upperCount = 0, spaceCount = 0;

            for (int i = 0; i < message.length(); i++) {
                char c = message.charAt(i);

                if (Character.isUpperCase(c)) {
                    upperCount++;
                }

                if (Character.isSpaceChar(c)) {
                    spaceCount++;
                }
            }

            if (upperCount / message.length() > mainConfig.getCapsThreshold()) {
                message = message.toLowerCase();
            }

            if (spaceCount / message.length() > mainConfig.getSpaceThreshold()) {
                throw new AntiSpamException("chat2go.chat.antispam.suspicious");
            }
        }

        return message;
    }

    public String formatMsg(String from, String to, String message) {
        String format = mainConfig.getPrivateMessageFormat();

        format = Chat2Go.parseHexColor(format);
        format = ChatColor.translateAlternateColorCodes('&', format);

        format = format.replace("{from}", from);
        format = format.replace("{to}", to);
        format = format.replace("{message}", message);

        return format;
    }

    public String formatBroadcast(String message) {
        String format = mainConfig.getBroadcastFormat();

        format = Chat2Go.parseHexColor(format);
        format = ChatColor.translateAlternateColorCodes('&', format);

        message = Chat2Go.parseHexColor(message);
        message = ChatColor.translateAlternateColorCodes('&', message);

        format = format.replace("{message}", message);

        return format;
    }

    public ArrayList<String> getBadWords() {
        return getBadWordConfig().getBadWords();
    }
}
