package eu.rex2go.chat2go.command.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Chat2GoTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chat2go")) {
            if (args.length == 0) {
                ArrayList<String> list = new ArrayList<>();

                list.add("help");
                list.add("reload");
                list.add("filter");
                list.add("slowmode");
                list.add("badwords");
                list.add("toggle");

                return list;
            }

            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("filter")) {
                if(args.length == 1) {
                    ArrayList<String> list = new ArrayList<>();

                    list.add("censor");
                    list.add("block");
                    list.add("disable");

                    return list;
                }
            } else if(subCommand.equalsIgnoreCase("badwords") || subCommand.equalsIgnoreCase("badword")) {
                if(args.length == 1) {
                    ArrayList<String> list = new ArrayList<>();

                    list.add("list");
                    list.add("add");
                    list.add("remove");
                    list.add("reload");

                    return list;
                }
            }
        }

        return null;
    }
}
