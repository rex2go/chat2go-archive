package eu.rex2go.chat2go.chat.exception;

import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;

public class BadWordException extends Exception {

    @Getter
    private String chatMessage;

    @Getter
    private ChatUser chatUser;

    public BadWordException(ChatUser chatUser, String chatMessage) {
        super(chatMessage);

        this.chatMessage = chatMessage;
        this.chatUser = chatUser;
    }
}
