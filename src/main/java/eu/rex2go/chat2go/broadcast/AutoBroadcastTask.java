package eu.rex2go.chat2go.broadcast;

import eu.rex2go.chat2go.Chat2Go;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoBroadcastTask extends BukkitRunnable {

    private Chat2Go plugin;

    public AutoBroadcastTask(Chat2Go plugin) {
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
    }
}
