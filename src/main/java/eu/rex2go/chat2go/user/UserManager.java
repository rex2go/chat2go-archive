package eu.rex2go.chat2go.user;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UserManager {

    @Getter
    private ArrayList<ChatUser> chatUsers = new ArrayList<>();

    public ChatUser getUser(final Player player) {
        return chatUsers.stream().filter(user -> user.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }
}
