package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.chat.FilterMode;
import lombok.Getter;
import lombok.Setter;

public class ConfigManager {

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

    @Getter
    private boolean slowModeEnabled = false;

    @Getter
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
    private boolean rangeChatEnabled = false;

    @Getter
    private int chatRange = 50;

    @Getter
    private boolean linkBlockEnabled = true;

    @Getter
    private boolean antiSpamEnabled = false;

    // TODO chat log, chat log length

    public void save() {

    }
}
