package eu.rex2go.chat2go.chat;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.user.User;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class JSONElement {

    @Getter
    private String id, text, hoverText, suggestCommand, runCommand, openUrl;

    public JSONElement(String id, String text, String hoverText, String suggestCommand, String runCommand,
                       String openUrl) {
        this.id = id;
        this.text = text;
        this.hoverText = hoverText;
        this.suggestCommand = suggestCommand;
        this.runCommand = runCommand;
        this.openUrl = openUrl;
    }

    public BaseComponent[] build(Chat2Go plugin, User user, Placeholder... placeholders) {
        String text = this.text;
        text = ChatColor.translateAlternateColorCodes('&', text);
        text = Chat2Go.parseHexColor(text);

        text = plugin.getChatManager().processPlaceholders(user, text, placeholders);

        BaseComponent[] baseComponents = TextComponent.fromLegacyText(text);

        for (BaseComponent textComponent : baseComponents) {
            if (hoverText != null && !hoverText.equals("")) {
                String hoverText = this.hoverText;

                hoverText = ChatColor.translateAlternateColorCodes('&', hoverText);
                hoverText = Chat2Go.parseHexColor(hoverText);
                hoverText = plugin.getChatManager().processPlaceholders(user, hoverText, placeholders);

                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new BaseComponent[]{new TextComponent(hoverText)}));
            }

            if (suggestCommand != null && !suggestCommand.equals("")) {
                String suggestCommand = this.suggestCommand;

                suggestCommand = plugin.getChatManager().processPlaceholders(user, suggestCommand, placeholders);

                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
            }

            if (runCommand != null && !runCommand.equals("")) {
                String runCommand = this.runCommand;

                runCommand = plugin.getChatManager().processPlaceholders(user, runCommand, placeholders);

                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, runCommand));
            }

            if (openUrl != null && !openUrl.equals("")) {
                String openUrl = this.openUrl;

                openUrl = plugin.getChatManager().processPlaceholders(user, openUrl, placeholders);

                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, openUrl));
            }
        }

        return baseComponents;
    }
}
