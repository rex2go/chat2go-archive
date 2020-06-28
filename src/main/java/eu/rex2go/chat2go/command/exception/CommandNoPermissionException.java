package eu.rex2go.chat2go.command.exception;

import lombok.Getter;

public class CommandNoPermissionException extends Exception {

    @Getter
    private String missingPermission;

    public CommandNoPermissionException(String missingPermission) {
        super(missingPermission); // TODO

        this.missingPermission = missingPermission;
    }
}
