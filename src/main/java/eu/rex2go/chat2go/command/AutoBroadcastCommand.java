package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.broadcast.AutoBroadcast;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNotANumberException;
import eu.rex2go.chat2go.command.exception.CommandWrongUsageException;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoBroadcastCommand extends WrappedCommandExecutor {

    public AutoBroadcastCommand(Chat2Go plugin) {
        super(plugin, "autobroadcast");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException, CommandWrongUsageException, CommandNotANumberException {
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

            // TODO pagination
            for (AutoBroadcast autoBroadcast : Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts()) {
                sender.sendMessage(Chat2Go.PREFIX + " #" + autoBroadcast.getId() + " " + ChatColor.WHITE
                        + autoBroadcast.getMessage() + ChatColor.GRAY + " (" + autoBroadcast.getInterval() + "s)"); //
                // formatting
            }
        } else if (subCommand.equalsIgnoreCase("add")) {
            if (args.length < 3) {
                throw new CommandWrongUsageException("/<command> add <interval> <message>");
            }

            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            message = new StringBuilder(message.substring(0, message.length() - 1));

            String intervalStr = args[1];

            // TODO e.g. 2m3s
            if (!MathUtil.isNumber(intervalStr)) {
                throw new CommandNotANumberException(intervalStr);
            }

            int interval = Integer.parseInt(intervalStr);
            int id = Chat2Go.getAutoBroadcastConfig().getNextId();

            AutoBroadcast autoBroadcast = new AutoBroadcast(id, interval, message.toString());

            Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().add(autoBroadcast);
            Chat2Go.getAutoBroadcastConfig().save();

            // TODO message
            Bukkit.broadcastMessage("add #" + id);
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                throw new CommandWrongUsageException("/<command> remove <id>");
            }

            String idStr = args[1];

            if (!MathUtil.isNumber(idStr)) {
                throw new CommandNotANumberException(idStr);
            }

            int id = Integer.parseInt(idStr);

            if(Chat2Go.getAutoBroadcastConfig().remove(id)) {
                Chat2Go.getAutoBroadcastConfig().save();
                Bukkit.broadcastMessage("remove #" + id); // TODO message
                return true;
            }

            // TODO message
            Bukkit.broadcastMessage("not found"); // TODO message
            return true;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloading", true,
                    Chat2Go.getAutoBroadcastConfig().getFileName());
            Chat2Go.getAutoBroadcastConfig().reload();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloaded", true,
                    Chat2Go.getAutoBroadcastConfig().getFileName());
            return true;
        }

        return false;
    }
}
