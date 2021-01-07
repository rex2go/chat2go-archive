package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.chat.JSONElementContent;
import eu.rex2go.chat2go.chat.Placeholder;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNoPlayerException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageCommand extends WrappedCommandExecutor {

    public MessageCommand(Chat2Go plugin) {
        super(plugin, "msg");
    }

    @Override
    protected boolean execute(CommandSender sender, User user, String label, String... args) throws
            CommandNoPermissionException,
            CommandPlayerNotOnlineException,
            CommandNoPlayerException,
            CommandCustomErrorException {

        checkPermission(sender, PermissionConstants.PERMISSION_COMMAND_MSG);
        checkPlayer(sender);

        Player player = user.getPlayer();

        if (args.length < 2) {
            // Send usage
            return false;
        }

        String targetName = args[0];
        if (targetName.equalsIgnoreCase(user.getName())) {
            throw new CommandCustomErrorException(Chat2Go.getMessageConfig().getMessage(
                    "chat2go.command.message.message_yourself"
            ));
        }

        // Message receiver
        Player targetPlayer =
                Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(targetName)).findFirst().orElse(null);

        if (targetPlayer == null) {
            throw new CommandPlayerNotOnlineException(targetName);
        }

        // Message receiver user
        User target = plugin.getUserManager().getUser(targetPlayer);
        target.setLastChatter(user);

        // Build message, skip username (first argument)
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        // Remove trailing white space
        message = new StringBuilder(message.substring(0, message.length() - 1));

        String formatted = plugin.getChatManager().formatMsg(message.toString(), user, target);

        if(Chat2Go.getMainConfig().isJsonElementsEnabled()) {
            Placeholder senderPlaceholder = new Placeholder("sender", user.getName(), true);
            Placeholder receiverPlaceholder = new Placeholder("receiver", target.getName(), true);

            formatted = plugin.getChatManager().processPlaceholders(
                    formatted,
                    user,
                    senderPlaceholder,
                    receiverPlaceholder);

            BaseComponent[] baseComponents = plugin.getChatManager().processJSONMessage(formatted, user);

            targetPlayer.spigot().sendMessage(baseComponents);
            player.spigot().sendMessage(baseComponents);

            return true;
        }

        targetPlayer.sendMessage(formatted);
        player.sendMessage(formatted);

        return true;
    }
}
