package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.GaMoFu.RaidBosses.RaidBosses;

public class DungeonControlTabComplete implements TabCompleter {

    private static final List<String> validSubcommands = Collections
            .unmodifiableList(Arrays.asList("stroll", "patrol", "lootchest"));

    private static final List<String> validStrollArgs = Collections
            .unmodifiableList(Arrays.asList("[<radius>]", "min", "max"));

    private static final List<String> validPatrolArgs = Collections.unmodifiableList(Arrays.asList("add", "show"));

    private final RaidBosses plugin;

    public DungeonControlTabComplete(RaidBosses plugin) {
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

            if (subCommand.equals("stroll")) {
                if (input.isEmpty())
                    return validStrollArgs;

                try {
                    Integer.parseInt(input);
                    return Arrays.asList("[<radius>]");
                } catch (NumberFormatException e) {
                }

                return validStrollArgs.stream().filter(strollArg -> strollArg.toUpperCase().startsWith(input))
                        .collect(Collectors.toList());

            } else if (subCommand.equals("patrol")) {
                return validPatrolArgs.stream().filter(patrolArg -> patrolArg.toUpperCase().startsWith(input))
                        .collect(Collectors.toList());
            }

        }

        if (arguments.length == 3) {
            if (!arguments[0].equals("stroll")) {
                return null;
            }

            if (!validStrollArgs.contains(arguments[1].toLowerCase())) {
                return null;
            }

            try {
                Integer.parseInt(arguments[2]);
                return Arrays.asList("[<value>]");
            } catch (NumberFormatException e) {
            }
        }

        return null;
    }

}
