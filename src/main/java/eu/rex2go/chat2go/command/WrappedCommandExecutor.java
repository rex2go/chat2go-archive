package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.command.exception.*;
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

        if (pluginCommand.getPermissionMessage() == null) {
            pluginCommand.setPermissionMessage(Chat2Go.getMessageConfig().getMessage("chat2go.command.error.no_permission"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(this.command)) {
            ChatUser user = null;
            if (sender instanceof Player) user = plugin.getUserManager().getUser((Player) sender);

            try {
                return execute(sender, user, label, args);
            } catch (CommandNoPermissionException exception) {
                Chat2Go.sendMessage(sender, "chat2go.command.error.no_permission", false);
            } catch (CommandPlayerNotOnlineException exception) {
                Chat2Go.sendMessage(sender, "chat2go.command.error.player_not_online", false, exception.getPlayerName());
            } catch (CommandWrongUsageException exception) {
                Chat2Go.sendMessage(sender, "chat2go.command.error.wrong_usage", false, exception.getUsage().replace("<command>", label));
            } catch (CommandCustomErrorException exception) {
                sender.sendMessage(ChatColor.RED + exception.getErrorMessage());
            } catch (CommandNoPlayerException exception) {
                Chat2Go.sendMessage(sender, "chat2go.command.error.no_player", false);
            } catch (CommandNotANumberException exception) {
                Chat2Go.sendMessage(sender, "chat2go.command.error.not_a_number", false, exception.getNumber());
            }

            return true;
        }

        return false;
    }

    public void checkAllPermissions(CommandSender sender, String... permissions) throws CommandNoPermissionException {
        for(String permission : permissions) {
            if(!sender.hasPermission(permission)) throw new CommandNoPermissionException(permission);
        }
    }

    public void checkPermission(CommandSender sender, String... permissions) throws CommandNoPermissionException {
        boolean check = false;

        for(String permission : permissions) {
            if(sender.hasPermission(permission)) {
                check = true;
                break;
            }
        }

        if(!check) throw new CommandNoPermissionException(permissions[0]);
    }

    protected abstract boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException, CommandPlayerNotOnlineException, CommandWrongUsageException, CommandCustomErrorException, CommandNoPlayerException, CommandNotANumberException;
}
