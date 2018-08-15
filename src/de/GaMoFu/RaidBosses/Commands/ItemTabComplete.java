package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.GaMoFu.RaidBosses.RaidBosses;

public class ItemTabComplete implements TabCompleter {

    private static final List<String> validSubcommands = Collections.unmodifiableList(Arrays.asList("give"));

    private final RaidBosses plugin;

    public ItemTabComplete(RaidBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        System.out.println("TabComplete for " + command.getName() + ". args (" + arguments.length + "): "
                + StringUtils.join(arguments, ","));

        if (arguments.length == 1) {
            String input = arguments[0];

            return validSubcommands.stream().filter(subCmd -> subCmd.startsWith(input)).collect(Collectors.toList());
        }

        if (arguments.length == 2) {
            String subCommand = arguments[0];
            String input = arguments[1].toUpperCase();

            if (!subCommand.equals("give")) {
                return null;
            }

            return plugin.getItemsFactory().getItemInternalNames().stream()
                    .filter(itemName -> itemName.toUpperCase().startsWith(input)).collect(Collectors.toList());

        }
        // TODO Auto-generated method stub
        return null;
    }

}
