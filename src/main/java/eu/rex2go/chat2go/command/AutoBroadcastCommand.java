package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.broadcast.AutoBroadcast;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNotANumberException;
import eu.rex2go.chat2go.command.exception.CommandWrongUsageException;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoBroadcastCommand extends WrappedCommandExecutor {

    public AutoBroadcastCommand(Chat2Go plugin) {
        super(plugin, "autobroadcast");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException, CommandWrongUsageException, CommandNotANumberException {
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
            for (AutoBroadcast autoBroadcast : Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts()) {
                sender.sendMessage(Chat2Go.PREFIX + " #" + autoBroadcast.getId() + " " + ChatColor.WHITE
                        + autoBroadcast.getMessage() + ChatColor.GRAY + " (interval: " + autoBroadcast.getInterval() +
                        "s, " +
                        "offset: " + autoBroadcast.getOffset() + "s)"); //
                // TODO formatting
            }
            return true;
        } else if (subCommand.equalsIgnoreCase("add")) {
            if (args.length < 4) {
                throw new CommandWrongUsageException("/<command> add <interval> <offset> <message>");
            }

            StringBuilder message = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            message = new StringBuilder(message.substring(0, message.length() - 1));

            String intervalStr = args[1];
            int interval = getSeconds(intervalStr);

            String offsetStr = args[2];
            int offset = getSeconds(offsetStr);

            int id = Chat2Go.getAutoBroadcastConfig().getNextId();

            AutoBroadcast autoBroadcast = new AutoBroadcast(id, interval, offset, message.toString());

            Chat2Go.getAutoBroadcastConfig().getAutoBroadcasts().add(autoBroadcast);
            Chat2Go.getAutoBroadcastConfig().save();

            Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.add", true, String.valueOf(id));
            return true;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                throw new CommandWrongUsageException("/<command> remove <id>");
            }

            String idStr = args[1];

            if (!MathUtil.isNumber(idStr)) {
                throw new CommandNotANumberException(idStr);
            }

            int id = Integer.parseInt(idStr);

            if (Chat2Go.getAutoBroadcastConfig().remove(id)) {
                Chat2Go.getAutoBroadcastConfig().save();
                Chat2Go.sendMessage(sender, "chat2go.command.autobroadcast.remove", true, String.valueOf(id));
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

        return false;
    }

    private int getSeconds(String timeString) {
        int seconds = 0;
        String mode = "s";
        String store = "";

        for (int i = timeString.length() - 1; i >= 0; i--) {
            char c = timeString.charAt(i);

            if (Character.isDigit(c)) {
                store = c + store;
            } else {
                if (!store.equalsIgnoreCase("")) {
                    if (mode.equalsIgnoreCase("s")) {
                        seconds += Integer.parseInt(store);
                    } else if (mode.equalsIgnoreCase("m")) {
                        seconds += Integer.parseInt(store) * 60;
                    } else if (mode.equalsIgnoreCase("h")) {
                        seconds += Integer.parseInt(store) * 60 * 60;
                    } else {
                        seconds += Integer.parseInt(store) * 60 * 60 * 24;
                    }
                    store = "";
                }

                if (c == 's' || c == 'S') {
                    mode = "s";
                } else if (c == 'm' || c == 'M') {
                    mode = "m";
                } else if (c == 'h' || c == 'H') {
                    mode = "h";
                } else if (c == 'd' || c == 'D') {
                    mode = "d";
                }
            }
        }

        if (!store.equalsIgnoreCase("")) {
            if (mode.equalsIgnoreCase("s")) {
                seconds += Integer.parseInt(store);
            } else if (mode.equalsIgnoreCase("m")) {
                seconds += Integer.parseInt(store) * 60;
            } else if (mode.equalsIgnoreCase("h")) {
                seconds += Integer.parseInt(store) * 60 * 60;
            } else {
                seconds += Integer.parseInt(store) * 60 * 60 * 24;
            }
        }

        return seconds;
    }
}
