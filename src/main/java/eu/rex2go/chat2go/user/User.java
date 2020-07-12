package eu.rex2go.chat2go.user;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.JSONElementContent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class User {

    @Getter
    private UUID uuid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String lastMessage = null;

    @Getter
    @Setter
    private long lastMessageTime = 0;

    @Getter
    @Setter
    private boolean muted = false;

    @Getter
    @Setter
    private long mutedTime = 0;

    @Getter
    @Setter
    private User lastChatter;

    @Getter
    @Setter
    private boolean badWordNotificationEnabled = true;

    @Getter
    @Setter
    private ArrayList<JSONElementContent> jsonContent = new ArrayList<>();

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public User(Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getPrefix() {
        if (Chat2Go.isVaultInstalled()) {
            return ChatColor.translateAlternateColorCodes('&', Chat2Go.getChat().getPlayerPrefix(getPlayer()));
        }

        return "";
    }

    public String getSuffix() {
        if (Chat2Go.isVaultInstalled()) {
            return ChatColor.translateAlternateColorCodes('&', Chat2Go.getChat().getPlayerSuffix(getPlayer()));
        }

        return "";
    }

    public void sendMessage(String key, boolean prefix, String... args) {
        if (getPlayer() == null) return;

        Chat2Go.sendMessage(getPlayer(), key, prefix, args);
    }


}
