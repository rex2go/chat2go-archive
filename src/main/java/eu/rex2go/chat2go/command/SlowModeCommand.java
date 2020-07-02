package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstant;
import eu.rex2go.chat2go.command.exception.*;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.ChatUser;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.command.CommandSender;

public class SlowModeCommand extends WrappedCommandExecutor {

    public SlowModeCommand(Chat2Go plugin) {
        super(plugin, "slowmode");
    }

    @Override
    protected boolean execute(CommandSender sender, ChatUser user, String label, String... args) throws CommandNoPermissionException,
            CommandNotANumberException {
        checkPermission(sender, PermissionConstant.PERMISSION_COMMAND_SLOW_MODE, PermissionConstant.PERMISSION_COMMAND_SLOWMODE);

        MainConfig mainConfig = Chat2Go.getMainConfig();
        String cooldownStr = null;

        if (args.length < 1) {
            cooldownStr = String.valueOf(mainConfig.getSlowModeSeconds());
        } else {
            cooldownStr = args[0];
        }

        if (!MathUtil.isNumber(cooldownStr)) {
            throw new CommandNotANumberException(cooldownStr);
        }

        int cooldown = Integer.parseInt(cooldownStr);
        boolean updatedState = !mainConfig.isSlowModeEnabled();

        mainConfig.setSlowModeEnabled(updatedState);
        mainConfig.setSlowModeSeconds(cooldown);

        Chat2Go.sendMessage(sender, "chat2go.command.slowmode." + (updatedState ? "enable" : "disable)"), true, String.valueOf(cooldown));
        return true;
    }
}
