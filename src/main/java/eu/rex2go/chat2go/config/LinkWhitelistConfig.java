package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LinkWhitelistConfig extends CustomConfig {

    @Getter
    private ArrayList<String> linkWhitelist = new ArrayList<>();

    public LinkWhitelistConfig(Chat2Go plugin) {
        super(plugin, "link-whitelist.yml");
    }

    @Override
    public void load() {
        List<String> linkWhitelist = (List<String>) getConfig().get("linkWhitelist");

        this.linkWhitelist.clear();

        if (linkWhitelist == null) return;

        this.linkWhitelist.addAll(linkWhitelist);
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    @Override
    public void save() {
        getConfig().set("linkWhitelist", linkWhitelist);
        saveConfig();
    }
}
