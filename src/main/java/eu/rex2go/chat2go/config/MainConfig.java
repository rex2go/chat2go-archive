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
    private boolean chatEnabled = true;

    @Getter
    private boolean configSync = false;

    @Getter
    private boolean chatFormatEnabled = true;

    @Getter
    private String chatFormat = "{prefix}{username}{suffix}&7: &f{message}";

    @Getter
    private boolean prefixTrailingSpaceEnabled = true;

    @Getter
    private boolean suffixLeadingSpaceEnabled = true;

    @Getter
    private boolean translateChatColorsEnabled = true;

    @Getter
    @Setter
    private boolean chatFilterEnabled = true;

    @Getter
    @Setter
    private FilterMode chatFilterMode = FilterMode.BLOCK;

    @Getter
    private boolean badWordNotificationEnabled = true;

    @Getter
    @Setter
    private boolean slowModeEnabled = false;

    @Getter
    @Setter
    private int slowModeSeconds = 5;

    @Getter
    private boolean hideJoinMessage = false;

    @Getter
    private boolean customJoinMessageEnabled = true;

    @Getter
    private String customJoinMessage = "&7[&a+&7] &7{username}";

    @Getter
    private boolean hideLeaveMessage = false;

    @Getter
    private boolean customLeaveMessageEnabled = true;

    @Getter
    private String customLeaveMessage = "&7[&c-&7] &7{username}";

    @Getter
    private String privateMessageFormat = "&8MSG &7[{from} -> {to}]&8: &f{message}";

    @Getter
    private boolean linkBlockEnabled = true;

    @Getter
    private boolean antiSpamEnabled = false;

    @Getter
    private double capsThreshold = 0.5;

    @Getter
    private double spaceThreshold = 0.3;

    @Getter
    private String broadcastFormat = "&f[&cBroadcast&f] {message}";

    @Getter
    private boolean statisticsAllowed = true;

    @Getter
    private boolean jsonElementsEnabled = false;

    @Getter // TODO config stuff
    private ArrayList<JSONElement> jsonElements = new ArrayList<>();

    // TODO chat log, chat log length, range chat, range chat length

    // version auch in config anpassen..
    public MainConfig(Chat2Go plugin) {
        super(plugin, "config.yml", 3);
    }

    @Override
    public void load() {
        chatEnabled = getConfig().getBoolean("chatEnabled");
        configSync = getConfig().getBoolean("configSync");
        chatFormatEnabled = getConfig().getBoolean("chatFormatEnabled");
        chatFormat = getConfig().getString("chatFormat");
        prefixTrailingSpaceEnabled = getConfig().getBoolean("prefixTrailingSpaceEnabled");
        suffixLeadingSpaceEnabled = getConfig().getBoolean("suffixLeadingSpaceEnabled");
        translateChatColorsEnabled = getConfig().getBoolean("translateChatColorsEnabled");

        String chatFilterString = getConfig().getString("chatFilterMode");
        if (chatFilterString != null) {
            try {
                chatFilterMode = FilterMode.valueOf(chatFilterString.toUpperCase());
            } catch (Exception exception) {
                // TODO message
                exception.printStackTrace();
            }
        }

        badWordNotificationEnabled = getConfig().getBoolean("badWordNotificationEnabled");
        slowModeEnabled = getConfig().getBoolean("slowModeEnabled");
        slowModeSeconds = getConfig().getInt("slowModeSeconds");
        hideJoinMessage = getConfig().getBoolean("hideJoinMessage");
        customJoinMessageEnabled = getConfig().getBoolean("customJoinMessageEnabled");
        customJoinMessage = getConfig().getString("customJoinMessage");
        hideLeaveMessage = getConfig().getBoolean("hideLeaveMessage");
        customLeaveMessageEnabled = getConfig().getBoolean("customLeaveMessageEnabled");
        customLeaveMessage = getConfig().getString("customLeaveMessage");
        privateMessageFormat = getConfig().getString("privateMessageFormat");
        linkBlockEnabled = getConfig().getBoolean("linkBlockEnabled");
        antiSpamEnabled = getConfig().getBoolean("antiSpamEnabled");
        capsThreshold = getConfig().getDouble("capsThreshold");
        spaceThreshold = getConfig().getDouble("spaceThreshold");
        broadcastFormat = getConfig().getString("broadcastFormat");
        statisticsAllowed = getConfig().getBoolean("statisticsAllowed");
        jsonElementsEnabled = getConfig().getBoolean("jsonElementsEnabled");

        try {
            jsonElements = new ArrayList<>();
            getConfig().getConfigurationSection("jsonElements").getKeys(false).forEach(id -> {
                try {
                    String text = getConfig().getString("jsonElements." + id + ".text");
                    String hoverText = getConfig().getString("jsonElements." + id + ".hoverText");
                    String suggestCommand = getConfig().getString("jsonElements." + id + ".suggestCommand");
                    String runCommand = getConfig().getString("jsonElements." + id + ".runCommand");
                    String openUrl = getConfig().getString("jsonElements." + id + ".openUrl");

                    JSONElement jsonElement = new JSONElement(id, text, hoverText, suggestCommand, runCommand, openUrl);
                    jsonElements.add(jsonElement);
                } catch (Exception exception) {
                    plugin.getLogger().log(Level.WARNING, "Error in " + getFileName());
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        if (!configSync) return;

        getConfig().set("chatEnabled", chatEnabled);
        getConfig().set("configSync", configSync);
        getConfig().set("chatFormatEnabled", chatFormatEnabled);
        getConfig().set("chatFormat", chatFormat);
        getConfig().set("prefixTrailingSpaceEnabled", prefixTrailingSpaceEnabled);
        getConfig().set("suffixLeadingSpaceEnabled", suffixLeadingSpaceEnabled);
        getConfig().set("translateChatColorsEnabled", translateChatColorsEnabled);
        getConfig().set("chatFilterMode", chatFilterMode.name());
        getConfig().set("badWordNotificationEnabled", badWordNotificationEnabled);
        getConfig().set("slowModeEnabled", slowModeEnabled);
        getConfig().set("slowModeSeconds", slowModeSeconds);
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

        // TODO save json

        super.save();
    }
}
