package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNoPlayerException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ClearChatCommand extends WrappedCommandExecutor {

    public ClearChatCommand(Chat2Go plugin) {
        super(plugin, "clearchat");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException, CommandCustomErrorException, CommandNoPlayerException {
        if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CLEAR_CHAT)) {
            throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CLEAR_CHAT);
        }

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage("  ");
        }

        return true;
    }
}
