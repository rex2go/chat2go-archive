package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.chat.FilterMode;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.config.ConfigManager;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Chat2GoCommand extends WrappedCommandExecutor {

    public Chat2GoCommand(Chat2Go plugin) {
        super(plugin, "chat2go");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException {
        if (!sender.hasPermission("chat2go.command")) {
            throw new CommandNoPermissionException("chat2go.command");
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("filter")) {
            if (!sender.hasPermission("chat2go.command.filter")) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleFilter(sender, user, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("badword")) {
            if (!sender.hasPermission("chat2go.command.badword")) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleBadWord(sender, user, args);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub command \"" + subCommand + "\". Type \"/chat help\" for help.");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "--- " + ChatColor.AQUA + "chat2go help" + ChatColor.GRAY + " ---");

        // TODO

        sender.sendMessage(ChatColor.GRAY + "---                  ---");
    }

    private void handleFilter(CommandSender sender, ChatUser user, String... args) {
        if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /chat filter <censor|block|disabled>");
            return;
        }

        String censorModeString = args[1].toUpperCase();
        ConfigManager configManager = plugin.getConfigManager();

        if (censorModeString.equalsIgnoreCase("disabled")) {
            configManager.setChatFilterEnabled(false);
            configManager.save();
        } else {
            try {
                FilterMode filterMode = FilterMode.valueOf(censorModeString);

                configManager.setChatFilterEnabled(true);
                configManager.setChatFilterMode(filterMode);
                configManager.save();
            } catch (Exception exception) {
                sender.sendMessage(ChatColor.RED + "Usage: /chat filter <censor|block|disabled>");
                return;
            }
        }

        sender.sendMessage(
                Chat2Go.PREFIX + " Chat filtering has been set to " + ChatColor.RED + censorModeString + ChatColor.GRAY + ".");
    }

    private void handleBadWord(CommandSender sender, ChatUser user, String... args) {
        if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /chat badword <list|add|remove>");
            return;
        }

        String subCommand = args[1];

        if (subCommand.equalsIgnoreCase("list")) {
            sender.sendMessage(Chat2Go.PREFIX + " The holy list of banned words:");

            if (plugin.getChatManager().getBadWords().isEmpty()) {
                sender.sendMessage(Chat2Go.PREFIX + ChatColor.RED + " This list is empty. You can add words to it " +
                        "using \"/chat badword add <word>\".");
                return;
            }

            for (String badWord : plugin.getChatManager().getBadWords()) {
                sender.sendMessage(Chat2Go.PREFIX + " - " + ChatColor.WHITE + badWord);
            }
            return;
        }

        // TODO add & remove
    }
}
