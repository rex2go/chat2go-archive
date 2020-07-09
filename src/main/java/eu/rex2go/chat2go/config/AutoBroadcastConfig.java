package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.broadcast.AutoBroadcast;
import lombok.Getter;

import java.util.ArrayList;
import java.util.logging.Level;

public class AutoBroadcastConfig extends CustomConfig {

    @Getter
    private ArrayList<AutoBroadcast> autoBroadcasts = new ArrayList<>();

    public AutoBroadcastConfig(Chat2Go plugin) {
        super(plugin, "auto-broadcast.yml");
    }

    @Override
    public void load() {
        try {
            getConfig().getConfigurationSection("autoBroadcasts").getKeys(false).forEach(id -> {
                try {
                    int idd = Integer.parseInt(id);
                    int interval = getConfig().getInt("autoBroadcasts." + id + ".interval");
                    String message = getConfig().getString("autoBroadcasts." + id + ".message");
                    int offset = getConfig().getInt("autoBroadcasts." + id + ".offset");

                    AutoBroadcast autoBroadcast = new AutoBroadcast(idd, interval, offset, message);

                    autoBroadcasts.add(autoBroadcast);
                } catch (NumberFormatException exception) {
                    plugin.getLogger().log(Level.WARNING, "Error in " + getFileName());
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        for (AutoBroadcast autoBroadcast : autoBroadcasts) {
            getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".interval", autoBroadcast.getInterval());
            getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".message", autoBroadcast.getMessage());
            getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".offset", autoBroadcast.getOffset());
        }
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

        getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".interval", null);
        getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".message", null);
        getConfig().set("autoBroadcasts." + autoBroadcast.getId() + ".offset", null);
        getConfig().set("autoBroadcasts." + autoBroadcast.getId(), null);

        autoBroadcasts.remove(autoBroadcast);

        save();
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
