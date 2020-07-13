package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.FilterMode;
import eu.rex2go.chat2go.chat.JSONElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.logging.Level;

public class MainConfig extends CustomConfig {

    @Getter
    @Setter
    @ConfigInfo(path = "general.chatEnabled")
    protected boolean chatEnabled = true;

    @Getter
    protected boolean configSync = false;

    @Getter
    @ConfigInfo(path = "chatFormatting.enabled")
    protected boolean chatFormatEnabled = true;

    @Getter
    @ConfigInfo(path = "chatFormatting.format")
    protected String chatFormat = "{prefix }{username}{ suffix}&7: &f{message}";

    @Getter
    @ConfigInfo(path = "chatFormatting.translateChatColors")
    protected boolean translateChatColorsEnabled = true;

    @Getter
    @Setter
    @ConfigInfo(path = "chatFilter.enabled")
    protected boolean chatFilterEnabled = true;

    @Getter
    @Setter
    @ConfigInfo(path = "chatFilter.filterMode")
    protected String chatFilterModeString = "BLOCK";

    @Getter
    @ConfigInfo(path = "mentions.badWordNotificationEnabled")
    protected boolean badWordNotificationEnabled = true;

    @Getter
    @Setter
    @ConfigInfo(path = "slowMode.enabled")
    protected boolean slowModeEnabled = false;

    @Getter
    @Setter
    @ConfigInfo(path = "slowMode.cooldown")
    protected int slowModeCooldown = 5;

    @Getter
    @ConfigInfo(path = "customJoinMessage.hidden")
    protected boolean hideJoinMessage = false;

    @Getter
    @ConfigInfo(path = "customJoinMessage.enabled")
    protected boolean customJoinMessageEnabled = true;

    @Getter
    @ConfigInfo(path = "customJoinMessage.message")
    protected String customJoinMessage = "&7[&a+&7] &7{username}";

    @Getter
    @ConfigInfo(path = "customLeaveMessage.hidden")
    protected boolean hideLeaveMessage = false;

    @Getter
    @ConfigInfo(path = "customLeaveMessage.enabled")
    private boolean customLeaveMessageEnabled = true;

    @Getter
    @ConfigInfo(path = "customLeaveMessage.message")
    protected String customLeaveMessage = "&7[&c-&7] &7{username}";

    @Getter
    @ConfigInfo(path = "formatting.privateMessage")
    protected String privateMessageFormat = "&8MSG &7[{sender} -> {receiver}]&8: &f{message}";

    @Getter
    @ConfigInfo(path = "chatFilter.linkBlockEnabled")
    protected boolean linkBlockEnabled = true;

    @Getter
    @ConfigInfo(path = "antiSpam.enabled")
    protected boolean antiSpamEnabled = false;

    @Getter
    @ConfigInfo(path = "antiSpam.capsThreshold")
    protected double capsThreshold = 0.5;

    @Getter
    @ConfigInfo(path = "antiSpam.spaceThreshold")
    protected double spaceThreshold = 0.3;

    @Getter
    @ConfigInfo(path = "formatting.broadcast")
    protected String broadcastFormat = "&f[&cBroadcast&f] {message}";

    @Getter
    @ConfigInfo(path = "general.statisticsAllowed")
    protected boolean statisticsAllowed = true;

    @Getter
    @ConfigInfo(path = "jsonElements.enabled")
    protected boolean jsonElementsEnabled = false;

    @Getter
    protected ArrayList<JSONElement> jsonElements = new ArrayList<>();

    @Getter
    @ConfigInfo(path = "mentions.chatMentionsEnabled")
    protected boolean chatMentionsEnabled = false;

    @Getter
    @ConfigInfo(path = "mentions.chatMentionSoundEnabled")
    protected boolean chatMentionSoundEnabled = false;

    // TODO chat log, chat log length, range chat, range chat length

    // version auch in config anpassen..
    public MainConfig(Chat2Go plugin) {
        super(plugin, "config.yml", 4);
    }

    @Override
    public void load() {
        /*chatEnabled = getConfig().getBoolean("general.chatEnabled");
        chatFormatEnabled = getConfig().getBoolean("chatFormatting.enabled");
        chatFormat = getConfig().getString("chatFormatting.format");
        translateChatColorsEnabled = getConfig().getBoolean("chatFormatting.translateChatColors");
        chatFilterEnabled = getConfig().getBoolean("chatFilter.enabled");

        String chatFilterString = getConfig().getString("chatFilter.filterMode");
        if (chatFilterString != null) {
            try {
                chatFilterMode = FilterMode.valueOf(chatFilterString.toUpperCase());
            } catch (Exception exception) {
                // TODO message
                exception.printStackTrace();
            }
        }

        badWordNotificationEnabled = getConfig().getBoolean("chatFilter.badWordNotificationEnabled");
        slowModeEnabled = getConfig().getBoolean("slowMode.enabled");
        slowModeCooldown = getConfig().getInt("slowMode.cooldown");
        hideJoinMessage = getConfig().getBoolean("customJoinMessage.hidden");
        customJoinMessageEnabled = getConfig().getBoolean("customJoinMessage.enabled");
        customJoinMessage = getConfig().getString("customJoinMessage.message");
        hideLeaveMessage = getConfig().getBoolean("customLeaveMessage.hidden");
        customLeaveMessageEnabled = getConfig().getBoolean("customLeaveMessage.enabled");
        customLeaveMessage = getConfig().getString("customLeaveMessage.message");
        privateMessageFormat = getConfig().getString("formatting.privateMessage");
        linkBlockEnabled = getConfig().getBoolean("chatFilter.linkBlockEnabled");
        antiSpamEnabled = getConfig().getBoolean("antiSpam.enabled");
        capsThreshold = getConfig().getDouble("antiSpam.capsThreshold");
        spaceThreshold = getConfig().getDouble("antiSpam.spaceThreshold");
        broadcastFormat = getConfig().getString("formatting.broadcast");
        statisticsAllowed = getConfig().getBoolean("general.statisticsAllowed");
        jsonElementsEnabled = getConfig().getBoolean("jsonElements.enabled");*/

        try {
            jsonElements = new ArrayList<>();
            getConfig().getConfigurationSection("jsonElements.elements").getKeys(false).forEach(id -> {
                try {
                    String text = getConfig().getString("jsonElements.elements." + id + ".text");
                    String hoverText = getConfig().getString("jsonElements.elements." + id + ".hoverText");
                    String suggestCommand = getConfig().getString("jsonElements.elements." + id + ".suggestCommand");
                    String runCommand = getConfig().getString("jsonElements.elements." + id + ".runCommand");
                    String openUrl = getConfig().getString("jsonElements.elements." + id + ".openUrl");

                    JSONElement jsonElement = new JSONElement(id, text, hoverText, suggestCommand, runCommand, openUrl);
                    jsonElements.add(jsonElement);
                } catch (Exception exception) {
                    plugin.getLogger().log(Level.WARNING, "Error in " + getFileName());
                }
            });
        } catch (Exception ignored) {
        }

        super.load();
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        /*if (!configSync) return;

        getConfig().set("chatEnabled", chatEnabled);
        getConfig().set("configSync", configSync);
        getConfig().set("chatFormatEnabled", chatFormatEnabled);
        getConfig().set("chatFormat", chatFormat);
        getConfig().set("translateChatColorsEnabled", translateChatColorsEnabled);
        getConfig().set("chatFilterMode", chatFilterMode.name());
        getConfig().set("badWordNotificationEnabled", badWordNotificationEnabled);
        getConfig().set("slowModeEnabled", slowModeEnabled);
        getConfig().set("slowModeSeconds", slowModeCooldown);
        getConfig().set("hideJoinMessage", hideJoinMessage);
        getConfig().set("customJoinMessageEnabled", customJoinMessageEnabled);
        getConfig().set("customJoinMessage", customJoinMessage);
        getConfig().set("hideLeaveMessage", hideLeaveMessage);
        getConfig().set("customLeaveMessageEnabled", customLeaveMessageEnabled);
        getConfig().set("customLeaveMessage", customLeaveMessage);
        getConfig().set("privateMessageFormat", privateMessageFormat);
        getConfig().set("linkBlockEnabled", linkBlockEnabled);
        getConfig().set("antiSpamEnabled", antiSpamEnabled);
        getConfig().set("capsThreshold", capsThreshold);
        getConfig().set("spaceThreshold", spaceThreshold);
        getConfig().set("broadcastFormat", broadcastFormat);
        getConfig().set("statisticsAllowed", statisticsAllowed);
        getConfig().set("jsonElementsEnabled", jsonElementsEnabled);

        // TODO save json*/

        super.save();
    }

    public FilterMode getChatFilterMode() {
        return FilterMode.valueOf(chatFilterModeString);
    }

    public void setChatFilterMode(FilterMode filterMode) {
        this.chatFilterModeString = filterMode.name();
    }
}
