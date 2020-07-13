package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.util.MathUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AutoBroadcastConfig extends CustomConfig {

    @Getter
    private ArrayList<String> autoBroadcasts = new ArrayList<>();

    @Getter @Setter
    private int time = 0;

    @Getter
    @Setter
    private int delay = 300;

    @Getter
    @Setter
    private boolean shuffle, allowShuffleDoubles = false;

    public AutoBroadcastConfig(Chat2Go plugin) {
        super(plugin, "auto-broadcast.yml");
    }

    @Override
    public void load() {
        try {
            List<String> autoBroadcasts = (List<String>) getConfig().get("autoBroadcasts");

            this.autoBroadcasts.clear();

            if (autoBroadcasts == null) return;

            this.autoBroadcasts.addAll(autoBroadcasts);
            this.delay = MathUtil.getSeconds(getConfig().getString("delay"));
            this.shuffle = getConfig().getBoolean("shuffle");
            this.allowShuffleDoubles = getConfig().getBoolean("allowShuffleDoubles");
        } catch (Exception exception) {
            plugin.getLogger().log(Level.WARNING, "Error in " + getFileName());
        }
    }

    @Override
    public void reload() {
        super.reload();
        load();
        this.time = delay;
    }

    @Override
    public void save() {
        getConfig().set("autoBroadcasts", autoBroadcasts);
        getConfig().set("delay", delay);
        getConfig().set("shuffle", shuffle);
        getConfig().set("allowShuffleDoubles", allowShuffleDoubles);
        super.save(getClass());
    }

    public void resetTime() {
        time = delay;
    }
}
