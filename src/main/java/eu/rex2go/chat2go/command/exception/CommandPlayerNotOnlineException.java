package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandPlayerNotOnlineException extends Exception {

    @Getter
    private String playerName;

    public CommandPlayerNotOnlineException(String playerName) {
        super(playerName); // TODO

        this.playerName = playerName;
    }
}
