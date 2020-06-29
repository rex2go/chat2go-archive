package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.command.exception.CommandWrongUsageException;
import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public abstract class WrappedCommandExecutor implements CommandExecutor {

    @Getter
    protected Chat2Go plugin;

    private String command;

    @Getter
    protected PluginCommand pluginCommand;

    public WrappedCommandExecutor(Chat2Go plugin, String command) {
        this.plugin = plugin;
        this.command = command;
        this.pluginCommand = Bukkit.getPluginCommand(command);

        pluginCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(this.command)) {
            ChatUser user = null;
            if(sender instanceof Player) user = plugin.getUserManager().getUser((Player) sender);

            try {
                return execute(sender, user, args);
            } catch (CommandNoPermissionException exception) {
                // TODO message
            } catch (CommandPlayerNotOnlineException exception) {
                sender.sendMessage(ChatColor.RED + exception.getPlayerName() + " is not online.");
                // TODO customizable
            } catch (CommandWrongUsageException exception) {
                sender.sendMessage(ChatColor.RED + "Usage: " + exception.getUsage().replace("<command>", label));
                // TODO customizable
            }

            return true;
        }

        return false;
    }

    protected abstract boolean execute(CommandSender sender, ChatUser user, String ... args) throws CommandNoPermissionException, CommandPlayerNotOnlineException, CommandWrongUsageException;
}
