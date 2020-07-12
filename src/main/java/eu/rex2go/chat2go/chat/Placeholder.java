package eu.rex2go.chat2go.chat;

import lombok.Getter;

public class Placeholder {

    @Getter
    boolean parseColor;
    @Getter
    private String key, replacement;

    public Placeholder(String key, String replacement, boolean parseColor) {
        this.key = key;
        this.replacement = replacement;
        this.parseColor = parseColor;
    }
}

