package eu.rex2go.chat2go.task;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.JSONElementContent;
import eu.rex2go.chat2go.config.AutoBroadcastConfig;
import eu.rex2go.chat2go.user.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class MainTask extends BukkitRunnable {

    private Chat2Go plugin;

    private AutoBroadcastConfig autoBroadcastConfig = Chat2Go.getAutoBroadcastConfig();

    private int autoBroadcastIndex = 0;

    private Random random = new Random();;

    public MainTask(Chat2Go plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if(autoBroadcastConfig.getAutoBroadcasts().size() > 0) {
            if (autoBroadcastConfig.getTime() > 0) {
                autoBroadcastConfig.setTime(autoBroadcastConfig.getTime() - 1);
            } else {
                autoBroadcastConfig.resetTime();
                int index = autoBroadcastIndex;

                if (autoBroadcastConfig.isShuffle() && autoBroadcastConfig.getAutoBroadcasts().size() > 1) {
                    index = random.nextInt(autoBroadcastConfig.getAutoBroadcasts().size());

                    if(index == autoBroadcastIndex) index++;
                } else {
                    index++;
                }

                if (autoBroadcastConfig.getAutoBroadcasts().size() <= index) {
                    index = 0;
                }

                String message = autoBroadcastConfig.getAutoBroadcasts().get(index);
                String formatted = plugin.getChatManager().formatBroadcast(message);

                Bukkit.broadcastMessage(formatted);

                autoBroadcastIndex = index;
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
