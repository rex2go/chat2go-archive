package eu.rex2go.chat2go.chat.exception;

import lombok.Getter;

public class AntiSpamException extends Exception {

    @Getter
    private String message;

    public AntiSpamException(String message) {
        super(message); // TODO
        this.message = message;
    }
}
