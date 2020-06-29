package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandWrongUsageException extends Exception {

    @Getter
    private String usage;

    public CommandWrongUsageException(String usage) {
        super(usage); // TODO

        this.usage = usage;
    }
}
