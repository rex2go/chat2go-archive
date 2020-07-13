package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BadWordConfig extends CustomConfig {

    @Getter
    private ArrayList<String> badWords = new ArrayList<>();

    public BadWordConfig(Chat2Go plugin) {
        super(plugin, "badwords.yml");
    }

    @Override
    public void load() {
        List<String> badWords = (List<String>) getConfig().get("badwords");

        this.badWords.clear();

        if (badWords == null) return;

        this.badWords.addAll(badWords);
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        getConfig().set("badwords", badWords);
        saveConfig();
    }
}
