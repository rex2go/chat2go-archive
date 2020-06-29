package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandCustomErrorException extends Exception {

    @Getter
    private String errorMessage;

    public CommandCustomErrorException(String errorMessage) {
        super(errorMessage); // TODO

        this.errorMessage = errorMessage;
    }
}
