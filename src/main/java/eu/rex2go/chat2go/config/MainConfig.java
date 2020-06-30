package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.FilterMode;
import lombok.Getter;
import lombok.Setter;

public class MainConfig extends CustomConfig {

    @Getter
    private boolean chatEnabled = true;

    @Getter
    private String chatFormat = "{prefix}{username}{suffix}&7: &f{message}";

    @Getter
    private boolean prefixTrailingSpaceEnabled = true;

    @Getter
    private boolean suffixLeadingSpaceEnabled = true;

    @Getter
    private boolean translateChatColorsEnabled = true;

    @Getter @Setter
    private boolean chatFilterEnabled = true;

    @Getter @Setter
    private FilterMode chatFilterMode = FilterMode.BLOCK;

    @Getter
    private boolean badWordNotificationEnabled = true;

    @Getter @Setter
    private boolean slowModeEnabled = false;

    @Getter @Setter
    private int slowModeSeconds = 5;

    @Getter
    private boolean customJoinMessageEnabled = true;

    @Getter
    private String customJoinMessage = "&7[&a+&7] &7{username}";

    @Getter
    private boolean customLeaveMessageEnabled = true;

    @Getter
    private String customLeaveMessage = "&7[&c-&7] &7{username}";

    @Getter
    private String privateMessageFormat = "&8MSG &7[{from} -> {to}]&8: &f{message}";

    @Getter
    private boolean linkBlockEnabled = true; // TODO

    @Getter
    private boolean antiSpamEnabled = false; // TODO

    // TODO chat log, chat log length, range chat, range chat length

    public MainConfig(Chat2Go plugin) {
        super(plugin, "config.yml");
    }

    @Override
    public void load() {
        chatEnabled = getConfig().getBoolean("chatEnabled");
        chatFormat = getConfig().getString("chatFormat");
        prefixTrailingSpaceEnabled = getConfig().getBoolean("prefixTrailingSpaceEnabled");
        suffixLeadingSpaceEnabled = getConfig().getBoolean("suffixLeadingSpaceEnabled");
        translateChatColorsEnabled = getConfig().getBoolean("translateChatColorsEnabled");

        String chatFilterString = getConfig().getString("chatFilterMode");
        if(chatFilterString != null) {
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
        customJoinMessageEnabled = getConfig().getBoolean("customJoinMessageEnabled");
        customJoinMessage = getConfig().getString("customJoinMessage");
        customLeaveMessageEnabled = getConfig().getBoolean("customLeaveMessageEnabled");
        customLeaveMessage = getConfig().getString("customLeaveMessage");
        privateMessageFormat = getConfig().getString("privateMessageFormat");
        linkBlockEnabled = getConfig().getBoolean("linkBlockEnabled");
        antiSpamEnabled = getConfig().getBoolean("antiSpamEnabled");
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        getConfig().set("chatEnabled", chatEnabled);
        getConfig().set("chatFormat", chatFormat);
        getConfig().set("prefixTrailingSpaceEnabled", prefixTrailingSpaceEnabled);
        getConfig().set("suffixLeadingSpaceEnabled", suffixLeadingSpaceEnabled);
        getConfig().set("translateChatColorsEnabled", translateChatColorsEnabled);
        getConfig().set("chatFilterMode", chatFilterMode.name());
        getConfig().set("badWordNotificationEnabled", badWordNotificationEnabled);
        getConfig().set("slowModeEnabled", slowModeEnabled);
        getConfig().set("slowModeSeconds", slowModeSeconds);
        getConfig().set("customJoinMessageEnabled", customJoinMessageEnabled);
        getConfig().set("customJoinMessage", customJoinMessage);
        getConfig().set("customLeaveMessageEnabled", customLeaveMessageEnabled);
        getConfig().set("customLeaveMessage", customLeaveMessage);
        getConfig().set("privateMessageFormat", privateMessageFormat);
        getConfig().set("linkBlockEnabled", linkBlockEnabled);
        getConfig().set("antiSpamEnabled", antiSpamEnabled);
        super.save();
    }
}
