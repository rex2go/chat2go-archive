package eu.rex2go.chat2go.command;

import eu.rex2go.chat2go.Chat2Go;
import eu.rex2go.chat2go.PermissionConstants;
import eu.rex2go.chat2go.command.exception.CommandNoPermissionException;
import eu.rex2go.chat2go.command.exception.CommandNotANumberException;
import eu.rex2go.chat2go.config.MainConfig;
import eu.rex2go.chat2go.user.User;
import eu.rex2go.chat2go.util.MathUtil;
import org.bukkit.command.CommandSender;

public class SlowModeCommand extends WrappedCommandExecutor {

    public SlowModeCommand(Chat2Go plugin) {
        super(plugin, "slowmode");
    }

    @Override
    protected boolean execute(CommandSender sender, User user, String label, String... args) throws CommandNoPermissionException,
            CommandNotANumberException {
        checkPermission(sender, PermissionConstants.PERMISSION_COMMAND_SLOW_MODE,
                PermissionConstants.PERMISSION_COMMAND_SLOWMODE);

        MainConfig mainConfig = Chat2Go.getMainConfig();
        String cooldownStr = null;

        if (args.length < 1) {
            cooldownStr = String.valueOf(mainConfig.getSlowModeCooldown());
        } else {
            cooldownStr = args[0];
        }

        if (!MathUtil.isNumber(cooldownStr)) {
            throw new CommandNotANumberException(cooldownStr);
        }

        int cooldown = Integer.parseInt(cooldownStr);
        boolean updatedState = !mainConfig.isSlowModeEnabled();

        mainConfig.setSlowModeEnabled(updatedState);
        mainConfig.setSlowModeCooldown(cooldown);

        Chat2Go.sendMessage(sender, "chat2go.command.slowmode." + (updatedState ? "enable" : "disable"), true,
                String.valueOf(cooldown));
        return true;
    }
}
