package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.command.exception.CommandCustomErrorException;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNoPlayerException;
import eu.rex2go.chat2go.command.exception.CommandPlayerNotOnlineException;
import eu.rex2go.chat2go.user.ChatUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends WrappedCommandExecutor {

    public ReplyCommand(Chat2Go plugin) {
        super(plugin, "reply");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException,
            CommandPlayerNotOnlineException, CommandCustomErrorException, CommandNoPlayerException {
        checkPermission(sender, PermissionConstant.PERMISSION_COMMAND_MSG);

        if (!(sender instanceof Player)) {
            throw new CommandNoPlayerException();
        }

        Player player = user.getPlayer();

        if (args.length < 1) {
            return false;
        }

        ChatUser target = user.getLastChatter();

        if (target == null) {
            throw new CommandCustomErrorException(Chat2Go.getMessageConfig().getMessage("chat2go.command.message" +
                    ".no_player_to_reply_to"));
        }

        Player targetPlayer = target.getPlayer();

        if (targetPlayer == null) {
            throw new CommandPlayerNotOnlineException(target.getName());
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
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
