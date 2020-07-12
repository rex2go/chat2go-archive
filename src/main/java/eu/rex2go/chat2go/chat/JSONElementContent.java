package eu.rex2go.chat2go.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class JSONElementContent {

    @Getter
    private UUID uuid;

    @Getter
    private JSONElement jsonElement;

    @Getter
    private Placeholder[] placeholders;

    @Getter
    @Setter
    private int ttl = 2;

    public JSONElementContent(UUID uuid, JSONElement jsonElement, Placeholder... placeholders) {
        this.uuid = uuid;
        this.jsonElement = jsonElement;
        this.placeholders = placeholders;
    }
}

