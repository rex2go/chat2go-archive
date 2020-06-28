package eu.rex2go.chat2go.command.exception;

public class CommandNoPermissionException extends Exception {

    public CommandNoPermissionException(String errorMessage) {
        super(errorMessage);
    }
}
