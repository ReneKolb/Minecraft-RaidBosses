package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.GaMoFu.RaidBosses.RaidBosses;

public class WorldTabComplete implements TabCompleter {

    private static final List<String> validSubcommands = Collections
            .unmodifiableList(Arrays.asList("create", "teleport", "remove", "list", "spawnpoint"));

    private final RaidBosses plugin;

    public WorldTabComplete(RaidBosses plugin) {
        this.plugin = plugin;
    }

    private List<String> getWorldsListWithHome() {
        List<String> result = new LinkedList<>();
        result.add("home");
        result.addAll(plugin.getWorlds().getWorlds());
        return result;
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

            if (subCommand.equals("create")) {
                return Arrays.asList(arguments[1], "[<name>]");

            } else if (subCommand.equals("teleport")) {
                return getWorldsListWithHome().stream().filter(world -> world.toUpperCase().startsWith(input))
                        .collect(Collectors.toList());
            } else if (subCommand.equals("remove")) {
                return plugin.getWorlds().getWorlds().stream().filter(world -> world.toUpperCase().startsWith(input))
                        .collect(Collectors.toList());
            }

        }

        return null;
    }

}
