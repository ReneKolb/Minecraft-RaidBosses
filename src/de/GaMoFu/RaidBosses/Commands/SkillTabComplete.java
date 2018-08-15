package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.GaMoFu.RaidBosses.RaidBosses;

public class SkillTabComplete implements TabCompleter {

    private static final List<String> validSubcommands = Collections.unmodifiableList(Arrays.asList("give"));

    private final RaidBosses plugin;

    public SkillTabComplete(RaidBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {

        if (arguments.length == 1) {
            String input = arguments[0].toUpperCase();

            return validSubcommands.stream().filter(subCmd -> subCmd.toUpperCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        if (arguments.length == 2) {
            String subCommand = arguments[0];
            String input = arguments[1].toUpperCase();

            if (!subCommand.equals("give")) {
                return null;
            }

            return plugin.getSkillFactory().getSkillInternalNames().stream()
                    .filter(itemName -> itemName.toUpperCase().startsWith(input)).collect(Collectors.toList());

        }

        return null;
    }

}
