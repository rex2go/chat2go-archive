package eu.rex2go.chat2go.command.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chat2GoTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chat2go")) {

            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>();

                list.add("help");
                list.add("reload");
                list.add("filter");
                list.add("slowmode");
                list.add("badword");
                list.add("toggle");

                List<String> suggest =
                        list.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());

                if (suggest.isEmpty()) return list;
                return suggest;
            }

            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("filter")) {
                if (args.length == 2) {
                    ArrayList<String> list = new ArrayList<>();

                    list.add("censor");
                    list.add("block");
                    list.add("disable");

                    List<String> suggest =
                            list.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());

                    if (suggest.isEmpty()) return list;
                    return suggest;
                }
            } else if (subCommand.equalsIgnoreCase("badwords") || subCommand.equalsIgnoreCase("badword")) {
                if (args.length == 2) {
                    ArrayList<String> list = new ArrayList<>();

                    list.add("list");
                    list.add("add");
                    list.add("remove");
                    list.add("reload");

                    List<String> suggest =
                            list.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());

                    if (suggest.isEmpty()) return list;
                    return suggest;
                }
            } else if(subCommand.equalsIgnoreCase("slowmode")) {
                ArrayList<String> list = new ArrayList<>();

                list.add("enable");
                list.add("disable");
                list.add("cooldown");

                List<String> suggest =
                        list.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());

                if (suggest.isEmpty()) return list;
                return suggest;
            }
        }

        return null;
    }
}
