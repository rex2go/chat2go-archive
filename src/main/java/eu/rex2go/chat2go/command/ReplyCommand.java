package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends WrappedCommandExecutor {

    public ReplyCommand(Chat2Go plugin) {
        super(plugin, "reply");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException {
        if (!(sender instanceof Player)) {
            // TODO message
            return true;
        }

        Player player = user.getPlayer();

        if (!player.hasPermission("chat2go.msg")) {
            throw new CommandNoPermissionException("chat2go.msg");
        }

        if (args.length < 1) {
            return false;
        }

        ChatUser target = user.getLastChatter();

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "No player to reply to.");
            return true;
        }

        Player targetPlayer = target.getPlayer();

        if (targetPlayer == null) {
            throw new CommandPlayerNotOnlineException(target.getName());
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        message = new StringBuilder(message.substring(0, message.length() - 1));

        String formatted = plugin.getChatManager().formatMsg(user.getName(), target.getName(),
                message.toString());

        target.setLastChatter(user);

        targetPlayer.sendMessage(formatted);
        player.sendMessage(formatted);

        return true;
    }
}
