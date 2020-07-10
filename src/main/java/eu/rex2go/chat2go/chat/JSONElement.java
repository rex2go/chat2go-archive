package eu.rex2go.chat2go.chat;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class JSONElement {

    @Getter
    private String id, text, hoverText, suggestCommand, runCommand, openUrl;


    public JSONElement(String id, String text, String hoverText, String suggestCommand, String runCommand, String openUrl) {
        this.id = id;
        this.text = text;
        this.hoverText = hoverText;
        this.suggestCommand = suggestCommand;
        this.runCommand = runCommand;
        this.openUrl = openUrl;
    }

    public TextComponent build(Chat2Go plugin, ChatUser user) {
        String text = this.text;
        text = ChatColor.translateAlternateColorCodes('&', text);
        text = Chat2Go.parseHexColor(text);
        text = plugin.getChatManager().processPlaceholders(user, text, "");

        TextComponent textComponent = new TextComponent(text);

        if (hoverText != null) {
            String hoverText = this.hoverText;
            hoverText = ChatColor.translateAlternateColorCodes('&', hoverText);
            hoverText = Chat2Go.parseHexColor(hoverText);

            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(hoverText)}));
        }

        if(suggestCommand != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        }

        if(runCommand != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, runCommand));
        }

        if(openUrl != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, openUrl));
        }

        return textComponent;
    }
}
