package eu.rex2go.chat2go.user;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UserManager {

    @Getter
    private ArrayList<User> users = new ArrayList<>();

    public User getUser(final Player player) {
        return users.stream().filter(user -> user.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }
}
