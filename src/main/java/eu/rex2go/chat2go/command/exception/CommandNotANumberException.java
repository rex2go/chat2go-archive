package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandNotANumberException extends Exception {

    @Getter
    private String number;

    public CommandNotANumberException(String number) {
        super(number); // TODO

        this.number = number;
    }
}
