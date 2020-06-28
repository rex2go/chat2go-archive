package eu.rex2go.chat2go;

import eu.rex2go.chat2go.chat.ChatManager;
import eu.rex2go.chat2go.command.Chat2GoCommand;
import eu.rex2go.chat2go.command.MessageCommand;
import eu.rex2go.chat2go.config.ConfigManager;
import eu.rex2go.chat2go.listener.PlayerChatListener;
import eu.rex2go.chat2go.listener.PlayerJoinListener;
import eu.rex2go.chat2go.listener.PlayerQuitListener;
import eu.rex2go.chat2go.user.UserManager;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Chat2Go extends JavaPlugin {

    public static final String PREFIX =
            ChatColor.WHITE + "[" + ChatColor.AQUA + "chat2go" + ChatColor.WHITE + "]" + ChatColor.GRAY;

    @Getter
    private static Chat chat;

    @Getter
    private static boolean vaultInstalled;

    @Getter
    private UserManager userManager;

    @Getter
    private ConfigManager configManager;

    @Getter
    private ChatManager chatManager;

    @Override
    public void onEnable() {
        vaultInstalled = Bukkit.getPluginManager().isPluginEnabled("Vault");

        if (vaultInstalled) {
            setupChat();
        } else {
            getLogger().log(Level.WARNING, "Vault is not installed. There will be no support for prefixes and " +
                    "suffixes.");
        }

        setupManagers();
        setupCommands();
        setupListeners();
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> chatProvider =
                getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
    }

    private void setupManagers() {
        userManager = new UserManager();
        configManager = new ConfigManager();
        chatManager = new ChatManager(this);
    }

    private void setupCommands() {
        new Chat2GoCommand(this);
        new MessageCommand(this);
    }

    private void setupListeners() {
        new PlayerChatListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }
}
