package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandPlayerNotFoundException extends Exception {

    @Getter
    private String playerName;

    public CommandPlayerNotFoundException(String playerName) {
        super(playerName); // TODO

        this.playerName = playerName;
    }
}
