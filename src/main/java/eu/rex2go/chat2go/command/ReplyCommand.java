package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.chat.Placeholder;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNoPlayerException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends WrappedCommandExecutor {

    public ReplyCommand(Chat2Go plugin) {
        super(plugin, "reply");
    }

    @Override
    protected boolean execute(CommandSender sender, User user, String label, String... args) throws
            CommandNoPermissionException,
            CommandPlayerNotOnlineException,
            CommandCustomErrorException,
            CommandNoPlayerException {

        checkPermission(sender, PermissionConstants.PERMISSION_COMMAND_MSG);
        checkPlayer(sender);

        Player player = user.getPlayer();

        if (args.length < 1) {
            // No message given, return false to send usage
            return false;
        }

        // User to reply to
        User target = user.getLastChatter();

        if (target == null) {
            throw new CommandCustomErrorException(
                    Chat2Go.getMessageConfig().getMessage(
                            "chat2go.command.message.no_player_to_reply_to"
                    )
            );
        }

        Player targetPlayer = target.getPlayer();

        if (targetPlayer == null) {
            throw new CommandPlayerNotOnlineException(target.getName());
        }

        // Build message
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        // Remove trailing white space
        message = new StringBuilder(message.substring(0, message.length() - 1));

        String formatted = plugin.getChatManager().formatMsg(message.toString(), user, target);

        target.setLastChatter(user);

        if(Chat2Go.getMainConfig().isJsonElementsEnabled()) {
            Placeholder senderPlaceholder = new Placeholder("sender", user.getName(), true);
            Placeholder receiverPlaceholder = new Placeholder("receiver", target.getName(), true);

            formatted = plugin.getChatManager().processPlaceholders(
                    formatted,
                    user,
                    senderPlaceholder,
                    receiverPlaceholder
            );

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
