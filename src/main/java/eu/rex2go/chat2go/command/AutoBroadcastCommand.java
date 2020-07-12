package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNotANumberException;
import eu.rex2go.chat2go.command.exception.CommandWrongUsageException;
import eu.rex2go.chat2go.user.User;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoBroadcastCommand extends WrappedCommandExecutor {

    public AutoBroadcastCommand(Chat2Go plugin) {
        super(plugin, "autobroadcast");
    }

    @Override
    protected boolean execute(CommandSender sender, User user, String label, String... args) throws CommandNoPermissionException, CommandWrongUsageException, CommandNotANumberException {
        checkPermission(sender, PermissionConstants.PERMISSION_COMMAND_AUTO_BROADCAST,
                PermissionConstants.PERMISSION_COMMAND_AUTOBROADCAST);

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

            // TODO pagination
            for (int i = 0; i < Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().size(); i++) {
                String autoBroadcast = Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().get(i);
                sender.sendMessage(Chat2Go.PREFIX + " #" + (i + 1) + " " + ChatColor.WHITE
                        + autoBroadcast);
                // TODO formatting
            }
            return true;
        } else if (subCommand.equalsIgnoreCase("add")) {
            if (args.length < 2) {
                throw new CommandWrongUsageException("/<command> add <message>");
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            message = new StringBuilder(message.substring(0, message.length() - 1));

            Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().add(message.toString());
            Chat2Go.getAutoBroadcastConfig().save();

            Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.add", true,
                    String.valueOf(Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().size()));
            return true;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                throw new CommandWrongUsageException("/<command> remove <id>");
            }

            String idStr = args[1];

            if (!MathUtil.isNumber(idStr)) {
                throw new CommandNotANumberException(idStr);
            }

            int id = Integer.parseInt(idStr) - 1;

            if(Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().size() >= id+1) {
                Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().remove(id);
                Chat2Go.getAutoBroadcastConfig().save();
                Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.remove", true, String.valueOf(id+1));
                return true;
            }

            Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.not_found", true, String.valueOf(id));
            return true;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloading", true,
                    Chat2Go.getAutoBroadcastConfig().getFileName());
            Chat2Go.getAutoBroadcastConfig().reload();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloaded", true,
                    Chat2Go.getAutoBroadcastConfig().getFileName());
            return true;
        }

        // TODO delay & shuffle

        return false;
    }


}
