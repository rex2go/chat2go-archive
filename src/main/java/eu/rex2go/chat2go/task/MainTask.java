package eu.rex2go.chat2go.task;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.broadcast.AutoBroadcast;
import eu.rex2go.chat2go.chat.JSONElementContent;
import eu.rex2go.chat2go.user.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MainTask extends BukkitRunnable {

    private Chat2Go plugin;

    public MainTask(Chat2Go plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (AutoBroadcast autoBroadcast : Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts()) {
            int remainingTime = autoBroadcast.getRemainingTime();

            if (remainingTime > 0) {
                autoBroadcast.setRemainingTime(--remainingTime);
            } else {
                String message = autoBroadcast.getMessage();

                message = plugin.getChatManager().formatBroadcast(message);

                Bukkit.broadcastMessage(message);

                autoBroadcast.resetRemainingTime();
            }
        }

        if (Chat2Go.getMainConfig().isJsonElementsEnabled()) {
            for (User user : plugin.getUserManager().getUsers()) {
                ArrayList<JSONElementContent> remove = new ArrayList<>();

                for (JSONElementContent content : user.getJsonContent()) {
                    int remainingTime = content.getTtl();

                    if (remainingTime > 0) {
                        content.setTtl(--remainingTime);
                    } else {
                        remove.add(content);
                    }
                }

                remove.forEach(user.getJsonContent()::remove);
            }
        }
    }
}
