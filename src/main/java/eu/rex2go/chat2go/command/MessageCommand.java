package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand extends WrappedCommandExecutor {

    public MessageCommand(Chat2Go plugin) {
        super(plugin, "msg");
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

        if (args.length < 2) {
            return false;
        }

        String targetName = args[0];
        if(targetName.equalsIgnoreCase(user.getName())) {
            // TODO message
            return true;
        }

        Player targetPlayer =
                Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(targetName)).findFirst().orElse(null);

        if (targetPlayer == null) {
            throw new CommandPlayerNotOnlineException(targetName);
        }

        ChatUser target = plugin.getUserManager().getUser(targetPlayer);
        target.setLastChatter(user);

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) {
                message.append(args[i]).append(" ");
            }
        }

        message = new StringBuilder(message.substring(0, message.length() - 1));

        String formatted = plugin.getChatManager().formatMsg(user.getName(), target.getName(),
                message.toString());
        targetPlayer.sendMessage(formatted);
        player.sendMessage(formatted);

        return true;
    }
}
