package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.broadcast.AutoBroadcast;
import lombok.Getter;

import java.util.ArrayList;

public class AutoBroadcastConfig extends CustomConfig {

    @Getter
    private ArrayList<AutoBroadcast> autoBroadcasts = new ArrayList<>();

    public AutoBroadcastConfig(Chat2Go plugin) {
        super(plugin, "auto-broadcast.yml");
    }

    @Override
    public void load() {
        // TODO
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        // TODO
        super.save();
    }

    public boolean remove(int id) {
        AutoBroadcast autoBroadcast = null;

        for (AutoBroadcast abcast : autoBroadcasts) {
            if (abcast.getId() == id) {
                autoBroadcast = abcast;
                break;
            }
        }

        if (autoBroadcast == null) {
            return false;
        }

        autoBroadcasts.remove(autoBroadcast);
        return true;
    }

    public int getNextId() {
        boolean found = false;
        int id = 1;

        while (!found) {
            boolean contains = false;

            for (AutoBroadcast abcast : autoBroadcasts) {
                if (abcast.getId() == id) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                found = true;
            } else {
                id++;
            }
        }

        return id;
    }
}
