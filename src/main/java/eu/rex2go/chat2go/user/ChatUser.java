package eu.rex2go.chat2go.user;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUser {

    @Getter
    private Player player;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String lastMessage = null;

    @Getter @Setter
    private long lastMessageTime = 0;

    @Getter @Setter
    private boolean muted = false;

    @Getter @Setter
    private long mutedTime = 0;

    @Getter @Setter
    private ChatUser lastChatter;

    public ChatUser(Player player, String name) {
        this.player = player;
        this.name = name;
    }

    public ChatUser(Player player) {
        this(player, player.getName());
    }

    public String getPrefix() {
        if(Chat2Go.isVaultInstalled()) {
            return ChatColor.translateAlternateColorCodes('&', Chat2Go.getChat().getPlayerPrefix(player));
        }

        return "";
    }

    public String getSuffix() {
        if(Chat2Go.isVaultInstalled()) {
            return ChatColor.translateAlternateColorCodes('&', Chat2Go.getChat().getPlayerSuffix(player));
        }

        return "";
    }
}
