package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ClearChatCommand extends WrappedCommandExecutor {

    public ClearChatCommand(Chat2Go plugin) {
        super(plugin, "clearchat");
    }

    @Override
    protected boolean execute(CommandSender sender, User user, String label, String... args) throws
            CommandNoPermissionException {

        checkPermission(sender, PermissionConstants.PERMISSION_COMMAND_CLEAR_CHAT);

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage("  ");
        }

        return true;
    }
}
