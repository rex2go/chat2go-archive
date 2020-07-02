package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.command.CommandSender;

public class AutoBroadcastCommand extends WrappedCommandExecutor {

    public AutoBroadcastCommand(Chat2Go plugin) {
        super(plugin, "autobroadcast");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException {
        checkPermission(sender, PermissionConstant.PERMISSION_COMMAND_AUTO_BROADCAST,
                PermissionConstant.PERMISSION_COMMAND_AUTOBROADCAST);

        if (args.length < 1) {
            return false;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("list")) {
            Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.list", true);

            if (Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().isEmpty()) {
                Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.list_empty", true, label);
                return true;
            }

            // TODO
        }

        return true;
    }
}
