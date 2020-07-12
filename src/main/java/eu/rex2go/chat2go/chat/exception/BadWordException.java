package eu.rex2go.chat2go.chat.exception;

import eu.rex2go.chat2go.user.User;
import lombok.Getter;

public class BadWordException extends Exception {

    @Getter
    private String chatMessage;

    @Getter
    private User user;

    @Getter
    private String message = "chat2go.chat.blocked_message";

    public BadWordException(User user, String chatMessage) {
        super(chatMessage); // TODO

        this.chatMessage = chatMessage;
        this.user = user;
    }
}
