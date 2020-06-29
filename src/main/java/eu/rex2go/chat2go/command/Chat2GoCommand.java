package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.FilterMode;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.command.exception.CommandWrongUsageException;
import eu.rex2go.chat2go.config.ConfigManager;
import eu.rex2go.chat2go.config.CustomConfig;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Chat2GoCommand extends WrappedCommandExecutor {

    public Chat2GoCommand(Chat2Go plugin) {
        super(plugin, "chat2go");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException, CommandWrongUsageException, CommandCustomErrorException {
        if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND)) {
            throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND);
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("filter")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_FILTER)) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleFilter(sender, user, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("badword")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_BAD_WORD)) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleBadWord(sender, user, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("slowmode")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_SLOW_MODE)) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleSlowMode(sender, user, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_RELOAD)) {
                sender.sendMessage(pluginCommand.getPermissionMessage());
                return true;
            }

            handleSlowMode(sender, user, args);
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

    private void handleFilter(CommandSender sender, ChatUser user, String... args) throws CommandWrongUsageException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> filter <censor|block|disable>");
        }

        String censorModeString = args[1].toUpperCase();
        ConfigManager configManager = plugin.getConfigManager();

        if (censorModeString.equalsIgnoreCase("disable")) {
            configManager.setChatFilterEnabled(false);
            configManager.save();
        } else {
            try {
                FilterMode filterMode = FilterMode.valueOf(censorModeString);

                configManager.setChatFilterEnabled(true);
                configManager.setChatFilterMode(filterMode);
                configManager.save();
            } catch (Exception exception) {
                throw new CommandWrongUsageException("/<command> filter <censor|block|disable>");
            }
        }

        sender.sendMessage(
                Chat2Go.PREFIX + " Chat filtering has been set to " + ChatColor.RED + censorModeString + ChatColor.GRAY + ".");
    }

    private void handleBadWord(CommandSender sender, ChatUser user, String... args) throws CommandWrongUsageException, CommandCustomErrorException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> badword <list|add|remove|reload>");
        }

        String subCommand = args[1];

        if (subCommand.equalsIgnoreCase("list")) {
            sender.sendMessage(Chat2Go.PREFIX + " The holy list of banned words:");

            if (plugin.getChatManager().getBadWords().isEmpty()) {
                sender.sendMessage(Chat2Go.PREFIX + ChatColor.RED + " This list is empty. You can add words to it " +
                        "using \"/chat badword add <word>\".");
                return;
            }

            // TODO pagination
            for (String badWord : plugin.getChatManager().getBadWords()) {
                sender.sendMessage(Chat2Go.PREFIX + " - " + ChatColor.WHITE + badWord);
            }
            return;
        } else if (subCommand.equalsIgnoreCase("add")) {
            if (args.length == 2) {
                throw new CommandWrongUsageException("/<command> badword add <word>");
            }

            if (plugin.getChatManager().getBadWords().stream().anyMatch(badWord -> badWord.equalsIgnoreCase(args[2]))) {
                throw new CommandCustomErrorException("Bad word is already on the list.");
            }

            plugin.getChatManager().getBadWords().add(args[2]);
            CustomConfig badWordConfig = plugin.getChatManager().getBadWordConfig();
            List<String> badWordList = (List<String>) badWordConfig.getConfig().getList("badwords");

            if (badWordList == null) badWordList = new ArrayList<>();

            badWordList.add(args[2]);
            badWordConfig.getConfig().set("badwords", badWordList);
            badWordConfig.save();
            sender.sendMessage(Chat2Go.PREFIX + " Added " + ChatColor.RED + "\"" + args[2] + "\" " + ChatColor.GRAY
                    + "to the bad word list.");
            return;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length == 2) throw new CommandWrongUsageException("/<command> badword remove <word>");

            if (plugin.getChatManager().getBadWords().stream().noneMatch(badWord -> badWord.equalsIgnoreCase(args[2]))) {
                throw new CommandCustomErrorException("Bad word is not on the list.");
            }

            plugin.getChatManager().getBadWords().remove(args[2]); // TODO case sensitivity
            CustomConfig badWordConfig = plugin.getChatManager().getBadWordConfig();
            List<String> badWordList = (List<String>) badWordConfig.getConfig().getList("badwords");

            if (badWordList == null) badWordList = new ArrayList<>();

            badWordList.remove(args[2]);
            badWordConfig.getConfig().set("badwords", badWordList);
            badWordConfig.save();
            sender.sendMessage(Chat2Go.PREFIX + " Removed " + ChatColor.RED + "\"" + args[2] + "\" " + ChatColor.GRAY
                    + "from the bad word list.");
            return;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            // TODO reload configs
            return;
        }

        throw new CommandWrongUsageException("/<command> badword <list|add|remove|reload>");
    }

    private void handleSlowMode(CommandSender sender, ChatUser user, String... args) throws CommandWrongUsageException, CommandCustomErrorException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> slowmode <enable|disable|cooldown>");
        }

        String subCommand = args[1];
        ConfigManager configManager = plugin.getConfigManager();

        if (subCommand.equalsIgnoreCase("enable")) {
            configManager.setSlowModeEnabled(true);
            configManager.save();

            sender.sendMessage(Chat2Go.PREFIX + " Chat Slow Mode has been " + ChatColor.GREEN + "enabled" + ChatColor.GREEN + ".");

            return;
        } else if (subCommand.equalsIgnoreCase("disable")) {
            configManager.setSlowModeEnabled(false);
            configManager.save();

            sender.sendMessage(Chat2Go.PREFIX + " Chat Slow Mode has been " + ChatColor.RED + "disabled" + ChatColor.GREEN + ".");

            return;
        } else if (subCommand.equalsIgnoreCase("cooldown")) {
            if (args.length == 2) {
                throw new CommandWrongUsageException("/<command> slowmode cooldown <seconds>");
            }

            String secondsStr = args[2];

            if (!MathUtil.isNumber(secondsStr)) {
                throw new CommandCustomErrorException("\"" + secondsStr + "\" is not a number."); // TODO own exception?
            }

            int seconds = Integer.parseInt(secondsStr);

            configManager.setSlowModeSeconds(seconds);
            configManager.save();

            sender.sendMessage(Chat2Go.PREFIX + " Chat cooldown set to " + seconds + "s.");

            if (!configManager.isSlowModeEnabled()) {
                sender.sendMessage(Chat2Go.PREFIX + " Chat Slow Mode is currently disabled. It will take " +
                        "effect when you enable it.");
            }
            return;
        }

        throw new CommandWrongUsageException("/<command> slowmode <enable|disable|cooldown>");
    }
}
