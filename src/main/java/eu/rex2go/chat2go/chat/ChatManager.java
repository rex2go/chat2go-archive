package eu.rex2go.chat2go.chat;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.chat.exception.AntiSpamException;
import eu.rex2go.chat2go.chat.exception.BadWordException;
import eu.rex2go.chat2go.config.BadWordConfig;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.User;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.*;
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

    public String format(String message, User processor) throws BadWordException, AntiSpamException {
        return format(message, processor, true, mainConfig.getChatFormat());
    }

    public String format(String message, User processor, boolean processMessage, String format) throws BadWordException,
            AntiSpamException {
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = Chat2Go.parseHexColor(format);

        if (processMessage) {
            format = format.replaceAll(Pattern.quote(" +"), " ");
            message = processMessage(message, processor);
        }

        Placeholder username = new Placeholder("username", processor.getName(), true);
        Placeholder prefix = new Placeholder("prefix", processor.getPrefix(), true);
        Placeholder suffix = new Placeholder("suffix", processor.getSuffix(), true);
        Placeholder messagePlaceholder = new Placeholder("message", message, false);

        format = processPlaceholders(format, processor, username, prefix, suffix,
                messagePlaceholder);

        return format.replace("%", "%%");
    }

    public String processPlaceholders(String format, User processor, Placeholder... placeholders) {
        if (Chat2Go.isPlaceholderInstalled()) {
            format = PlaceholderAPI.setPlaceholders(processor.getPlayer(), format);
            format = ChatColor.translateAlternateColorCodes('&', format);
            format = Chat2Go.parseHexColor(format);
        }

        Pattern pattern = Pattern.compile("\\{( *)(.*?)( *)}");
        Matcher matcher = pattern.matcher(format);

        while (matcher.find()) {
            String match = matcher.group(0);
            String leadingSpaces = matcher.group(1);
            String placeholder = matcher.group(2);
            String trailingSpaces = matcher.group(3);
            String placeholderContent = placeholder;

            for (Placeholder placeholder1 : placeholders) {
                String key = placeholder1.getKey();
                String value = placeholder1.getReplacement();

                if (placeholder.equalsIgnoreCase(key)) {
                    if (placeholder1.isParseColor()) {
                        value = ChatColor.translateAlternateColorCodes('&', value);
                        value = Chat2Go.parseHexColor(value);
                    }

                    placeholderContent = value;
                    break;
                }
            }

            if (placeholderContent.equals(placeholder)) {
                for (JSONElement jsonElement : mainConfig.getJsonElements()) {
                    if (placeholder.equalsIgnoreCase(jsonElement.getId())) {
                        if (mainConfig.isJsonElementsEnabled()) {
                            UUID uuid = UUID.randomUUID();
                            JSONElementContent jsonElementContent = new JSONElementContent(
                                    uuid, jsonElement, placeholders);
                            processor.getJsonContent().add(jsonElementContent);
                            placeholderContent = "{" + uuid.toString() + "}";
                        }
                        break;
                    }
                }
            }

            if (placeholderContent.equals("")) {
                format = format.replace(match, "");
                continue;
            }

            format = format.replace(match, leadingSpaces + placeholderContent + trailingSpaces);
        }

        return format;

    }

    /**
     * @param message the message to process
     * @param sender the sender of the message
     * @return the processed message
     */
    public String processMessage(String message, User sender) throws BadWordException, AntiSpamException {
        message = antiSpamCheck(message, sender);
        String[] ads = new String[0];

        if (!sender.getPlayer().hasPermission(PermissionConstants.PERMISSION_CHAT_BYPASS_IP)) {
            ads = filterAdvertisement(message);
        }

        sender.setLastMessage(message);
        sender.setLastMessageTime(System.currentTimeMillis());

        if (mainConfig.isTranslateChatColorsEnabled()
                && sender.getPlayer().hasPermission(PermissionConstants.PERMISSION_CHAT_COLOR)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = Chat2Go.parseHexColor(message);
        }

        if (!message.equals(filter(message, ads))
                && !sender.getPlayer().hasPermission(PermissionConstants.PERMISSION_BAD_WORD_IGNORE)
                && mainConfig.isChatFilterEnabled()) {
            if (mainConfig.isBadWordNotificationEnabled()) {
                for (User staff : plugin.getUserManager().getUsers()) {
                    if (staff.getPlayer().hasPermission(PermissionConstants.PERMISSION_BAD_WORD_NOTIFY)
                            && sender.isBadWordNotificationEnabled()) {
                        staff.getPlayer().sendMessage(
                                Chat2Go.PREFIX + " " + Chat2Go.WARNING_PREFIX + " " + sender.getName() + ": " + ChatColor.RED + message);
                    }
                }
            }

            if (mainConfig.getChatFilterMode() == FilterMode.CENSOR) {
                message = filter(message, ads);
            } else if (mainConfig.getChatFilterMode() == FilterMode.BLOCK) {
                throw new BadWordException(sender, message);
            }
        }


        return message;
    }

    /**
     * @param message the message to filter
     * @param additional additional filter strings
     * @return the filtered message
     */
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

    /**
     * @param message the message to filter
     * @return an array of advertisement strings
     */
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

            pattern = Pattern.compile("([^ ]*\\.[^0-9]{1,4}\\b)");
            matcher = pattern.matcher(message.toLowerCase());

            while (matcher.find()) {
                String link = matcher.group().replace(" ", "");

                if (Chat2Go.getLinkWhitelistConfig().getLinkWhitelist().stream().noneMatch(link::equalsIgnoreCase)) {
                    ads.add(link);
                }
            }
        }

        return ads.toArray(new String[0]);
    }

    /**
     * @param sender the sender of the message
     * @param message the message to check
     * @return the checked message
     */
    private String antiSpamCheck(String message, User sender) throws AntiSpamException {
        if (!mainConfig.isAntiSpamEnabled()) {
            return message;
        }

        if (sender.hasPermission(PermissionConstants.PERMISSION_CHAT_BYPASS_ANTI_SPAM)) {
            return message;
        }

        double diff = ((System.currentTimeMillis() - sender.getLastMessageTime()) / 1000D);

        if (diff < 0.5) {
            throw new AntiSpamException("chat2go.chat.antispam.too_fast");
        }

        if (diff < 30) {
            if (sender.getLastMessage().equalsIgnoreCase(message)) {
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

    /**
     * @param message the string to format
     * @param sender the sender
     * @param receiver the receiver
     * @return the formatted string
     */
    public String formatMsg(String message, User sender, User receiver) {
        String format = mainConfig.getPrivateMessageFormat();

        format = Chat2Go.parseHexColor(format);
        format = ChatColor.translateAlternateColorCodes('&', format);

        format = format.replace("{sender}", sender.getName());
        format = format.replace("{receiver}", receiver.getName());
        format = format.replace("{message}", message);

        return format;
    }

    /**
     * @param message the message to format
     * @return the formatted string
     */
    public String formatBroadcast(String message) {
        String format = mainConfig.getBroadcastFormat();

        format = format.replace("{message}", message);
        format = Chat2Go.parseHexColor(format);
        format = ChatColor.translateAlternateColorCodes('&', format);

        return format;
    }

    public ArrayList<String> getBadWords() {
        return getBadWordConfig().getBadWords();
    }

    /**
     * @param message the format containing json placeholders to process
     * @param processor the user containing the processing content ({@link JSONElementContent})
     * @return an array of {@link BaseComponent}
     */
    public BaseComponent[] processJSONMessage(String message, User processor) {
        ArrayList<BaseComponent> baseComponents = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*?)\\{( *)(.*?)( *)}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String match = matcher.group(0).replace("%%", "%");
            String before = matcher.group(1).replace("%%", "%");
            String spaceBefore = matcher.group(2).replace("%%", "%");
            String placeholder = matcher.group(3).replace("%%", "%");
            String spaceAfter = matcher.group(4).replace("%%", "%");
            Optional<JSONElementContent> contentOptional = processor.getJsonContent().stream().filter(
                    c -> placeholder.equals(c.getUuid().toString())).findFirst();

            if (contentOptional.isPresent()) {
                JSONElementContent content = contentOptional.get();

                if (placeholder.equals(content.getUuid().toString())) {
                    BaseComponent[] textComponent = content.getJsonElement()
                            .build(plugin, processor, spaceBefore, spaceAfter, content.getPlaceholders());
                    BaseComponent[] beforeComponent = TextComponent.fromLegacyText(before);

                    if (textComponent[0].getColorRaw() == null) {
                        textComponent[0].setColor(beforeComponent[beforeComponent.length - 1].getColor());
                    }

                    baseComponents.addAll(Arrays.asList(beforeComponent));
                    baseComponents.addAll(Arrays.asList(textComponent));
                }
            } else {
                baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(match)));
            }
        }

        pattern = Pattern.compile("}?([^{}]*?$)");
        matcher = pattern.matcher(message);

        // TODO test PlaceholderAPI
        while (matcher.find()) {
            String after = matcher.group(1).replace("%%", "%");
            BaseComponent[] beforeComponent = TextComponent.fromLegacyText(after);
            baseComponents.addAll(Arrays.asList(beforeComponent));
        }

        processor.getJsonContent().clear();

        return baseComponents.toArray(new BaseComponent[0]);
    }
}
