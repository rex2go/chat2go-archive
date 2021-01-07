package eu.rex2go.chat2go;

public class PermissionConstants {

    public static final String PERMISSION_CHAT_COLOR = "chat2go.chat.color";
    public static final String[] PERMISSION_CHAT_BYPASS_SLOW_MODE = new String[] { "chat2go.chat.bypass_slow_mode", "chat2go.chat.bypass_slowmode" };
    public static final String[] PERMISSION_CHAT_BYPASS_ANTI_SPAM = new String[] { "chat2go.chat.bypass_anti_spam", "chat2go.chat.bypass_antispam" };
    public static final String PERMISSION_CHAT_BYPASS_IP = "chat2go.chat.bypass_ip";

    public static final String PERMISSION_BAD_WORD_IGNORE = "chat2go.bad_word.ignore";
    public static final String PERMISSION_BAD_WORD_NOTIFY = "chat2go.bad_word.notify";

    // command permissions

    public static final String PERMISSION_COMMAND_CHAT = "chat2go.command.chat";
    public static final String PERMISSION_COMMAND_CHAT_FILTER = "chat2go.command.chat.filter";
    public static final String[] PERMISSION_COMMAND_CHAT_BAD_WORD = new String[] { "chat2go.command.chat.bad_word", "chat2go.command.chat.badword" };
    public static final String[] PERMISSION_COMMAND_CHAT_SLOW_MODE = new String[] { "chat2go.command.chat.slow_mode", "chat2go.command.chat.slowmode" };
    public static final String PERMISSION_COMMAND_CHAT_TOGGLE = "chat2go.command.chat.toggle";
    public static final String PERMISSION_COMMAND_CHAT_RELOAD = "chat2go.command.chat.reload";

    public static final String[] PERMISSION_COMMAND_CLEAR_CHAT = new String [] { "chat2go.command.clear_chat", "chat2go.command.clearchat" };

    public static final String PERMISSION_COMMAND_MSG = "chat2go.command.msg";

    public static final String[] PERMISSION_COMMAND_SLOW_MODE = new String[] { "chat2go.command.slow_mode", "chat2go.command.slowmode" };

    public static final String PERMISSION_COMMAND_BROADCAST = "chat2go.command.broadcast";
    public static final String[] PERMISSION_COMMAND_AUTO_BROADCAST = new String[] { "chat2go.command.auto_broadcast", "chat2go.command.autobroadcast" };
}
