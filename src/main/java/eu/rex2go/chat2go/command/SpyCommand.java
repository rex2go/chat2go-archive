package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.command.CommandSender;

public class SpyCommand extends WrappedCommandExecutor {

    public SpyCommand(Chat2Go plugin) {
        super(plugin, "spy");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException, CommandCustomErrorException {
        if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_SPY)) {
            throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_SPY);
        }

        // TODO

        return true;
    }
}
