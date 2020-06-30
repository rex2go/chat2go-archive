package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.chat.FilterMode;
import eu.rex2go.chat2go.command.exception.*;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Chat2GoCommand extends WrappedCommandExecutor {

    public Chat2GoCommand(Chat2Go plugin) {
        super(plugin, "chat2go");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException, CommandWrongUsageException, CommandCustomErrorException,
            CommandNotANumberException {
        if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CHAT)) {
            throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CHAT);
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("filter")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CHAT_FILTER)) {
                throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CHAT_FILTER);
            }

            handleFilter(sender, user, label, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("badword")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CHAT_BAD_WORD)) {
                throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CHAT_BAD_WORD);
            }

            handleBadWord(sender, user, label, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("slowmode")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CHAT_SLOW_MODE)) {
                throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CHAT_SLOW_MODE);
            }

            handleSlowMode(sender, user, label, args);
            return true;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(PermissionConstant.PERMISSION_COMMAND_CHAT_RELOAD)) {
                throw new CommandNoPermissionException(PermissionConstant.PERMISSION_COMMAND_CHAT_RELOAD);
            }

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloading", true,
                    Chat2Go.getMainConfig().getFileName());
            Chat2Go.getMainConfig().reload();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloading", true,
                    Chat2Go.getBadWordConfig().getFileName());
            Chat2Go.getBadWordConfig().reload();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.reloading", true,
                    Chat2Go.getMessageConfig().getFileName());
            Chat2Go.getMessageConfig().reload();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.reload.done", true);
            return true;
        }

        throw new CommandCustomErrorException(Chat2Go.getMessageConfig().getMessage("chat2go.command.chat" +
                        ".unknown_sub_command",
                subCommand, label));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "--- " + ChatColor.AQUA + "chat2go help" + ChatColor.GRAY + " ---");

        // TODO

        sender.sendMessage(ChatColor.GRAY + "---                  ---");
    }

    private void handleFilter(CommandSender sender, ChatUser user, String label, String... args) throws CommandWrongUsageException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> filter <censor|block|disable>");
        }

        String censorModeString = args[1].toUpperCase();
        MainConfig mainConfig = Chat2Go.getMainConfig();

        if (censorModeString.equalsIgnoreCase("disable")) {
            mainConfig.setChatFilterEnabled(false);
            mainConfig.save();
        } else {
            try {
                FilterMode filterMode = FilterMode.valueOf(censorModeString);

                mainConfig.setChatFilterEnabled(true);
                mainConfig.setChatFilterMode(filterMode);
                mainConfig.save();
            } catch (Exception exception) {
                throw new CommandWrongUsageException("/<command> filter <censor|block|disable>");
            }
        }

        Chat2Go.sendMessage(sender, "chat2go.command.chat.filter.set_to", true, censorModeString);
    }

    private void handleBadWord(CommandSender sender, ChatUser user, String label, String... args) throws CommandWrongUsageException, CommandCustomErrorException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> badword <list|add|remove|reload>");
        }

        String subCommand = args[1];

        if (subCommand.equalsIgnoreCase("list")) {
            Chat2Go.sendMessage(sender, "chat2go.command.chat.badword.list", true);

            if (plugin.getChatManager().getBadWords().isEmpty()) {
                Chat2Go.sendMessage(sender, "chat2go.command.chat.badword.list_empty", true, label);
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
                throw new CommandCustomErrorException(Chat2Go.getMessageConfig().getMessage("chat2go.command.chat" +
                        ".badword.error.already_on_the_list"));
            }

            plugin.getChatManager().getBadWords().add(args[2]);
            plugin.getChatManager().getBadWordConfig().save();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.badword.add", true, args[2]);
            return;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                throw new CommandWrongUsageException("/<command> badword remove <word>");
            }

            if (plugin.getChatManager().getBadWords().stream().noneMatch(badWord -> badWord.equalsIgnoreCase(args[2]))) {
                throw new CommandCustomErrorException(Chat2Go.getMessageConfig().getMessage("chat2go.command.chat" +
                        ".badword.error.not_on_the_list"));
            }

            plugin.getChatManager().getBadWords().remove(args[2]); // TODO case sensitivity
            plugin.getChatManager().getBadWordConfig().save();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.badword.remove", true, args[2]);
            return;
        } else if (subCommand.equalsIgnoreCase("reload")) {
            // TODO reload configs
            return;
        }

        throw new CommandWrongUsageException("/<command> badword <list|add|remove|reload>");
    }

    private void handleSlowMode(CommandSender sender, ChatUser user, String label, String... args) throws CommandWrongUsageException, CommandCustomErrorException, CommandNotANumberException {
        if (args.length == 1) {
            throw new CommandWrongUsageException("/<command> slowmode <enable|disable|cooldown>");
        }

        String subCommand = args[1];
        MainConfig mainConfig = Chat2Go.getMainConfig();

        if (subCommand.equalsIgnoreCase("enable")) {
            mainConfig.setSlowModeEnabled(true);
            mainConfig.save();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.slowmode.enable", true);

            return;
        } else if (subCommand.equalsIgnoreCase("disable")) {
            mainConfig.setSlowModeEnabled(false);
            mainConfig.save();

            Chat2Go.sendMessage(sender, "chat2go.command.chat.slowmode.disable", true);

            return;
        } else if (subCommand.equalsIgnoreCase("cooldown")) {
            if (args.length == 2) {
                throw new CommandWrongUsageException("/<command> slowmode cooldown <seconds>");
            }

            String secondsStr = args[2];

            if (!MathUtil.isNumber(secondsStr)) {
                throw new CommandNotANumberException(secondsStr);
            }

            int seconds = Integer.parseInt(secondsStr);

            mainConfig.setSlowModeSeconds(seconds);
            mainConfig.save();

            // TODO customizable
            sender.sendMessage(Chat2Go.PREFIX + " Chat cooldown set to " + seconds + "s.");

            if (!mainConfig.isSlowModeEnabled()) {
                // TODO customizable
                sender.sendMessage(Chat2Go.PREFIX + " Chat Slow Mode is currently disabled. It will take " +
                        "effect when you enable it.");
            }
            return;
        }

        throw new CommandWrongUsageException("/<command> slowmode <enable|disable|cooldown>");
    }
}
