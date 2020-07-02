package eu.rex2go.chat2go.chat.exception;

import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;

public class BadWordException extends Exception {

    @Getter
    private String chatMessage;

    @Getter
    private ChatUser chatUser;

    @Getter
    private String message = "chat2go.chat.blocked_message";

    public BadWordException(ChatUser chatUser, String chatMessage) {
        super(chatMessage); // TODO

        this.chatMessage = chatMessage;
        this.chatUser = chatUser;
    }
}
