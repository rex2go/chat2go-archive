package eu.rex2go.chat2go.config;

import eu.rex2go.chat2go.Chat2Go;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MessageConfig extends CustomConfig {

    @Getter
    private HashMap<String, String> messages = new HashMap<>();

    public MessageConfig(Chat2Go plugin) {
        super(plugin, "messages.yml");
    }

    @Override
    public void load() {
        Map<String, Object> values = getConfig().getValues(true);

        messages.clear();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                messages.put(key, (String) value);
            }
        }
    }

    @Override
    public void reload() {
        super.reload();
        load();
    }

    public String getMessage(String key, String... args) {
        if (!messages.containsKey(key)) {
            plugin.getLogger().log(Level.WARNING, "Missing message: " + key);
            return key;
        }
        String message = messages.get(key);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            message = message.replace("{" + i + "}", arg);
        }

        message = Chat2Go.parseHexColor(message);

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }
}
