package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.user.ChatUser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class WrappedCommandExecutor implements CommandExecutor {

    @Getter
    private Chat2Go plugin;

    private String command;

    public WrappedCommandExecutor(Chat2Go plugin, String command) {
        this.command = command;
        Bukkit.getPluginCommand(command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(this.command)) {
            ChatUser user = null;
            if(sender instanceof Player) user = plugin.getUserManager().getUser((Player) sender);

            execute(sender, user, args);
            return true;
        }
        return false;
    }

    protected abstract void execute(CommandSender sender, ChatUser user, String ... args);
}
