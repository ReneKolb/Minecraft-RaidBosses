package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Dungeons.Dungeon;

public class ShowTabCompleter implements TabCompleter {

    private static final List<String> validSubcommands = Collections.unmodifiableList(Arrays.asList("loc"));

    private final RaidBosses plugin;

    public ShowTabCompleter(RaidBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {

        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;

        if (arguments.length == 1) {
            String input = arguments[0].toUpperCase();

            return validSubcommands.stream().filter(subCmd -> subCmd.toUpperCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        if (arguments.length == 2) {
            String subCommand = arguments[0];
            String input = arguments[1].toUpperCase();

            if (!subCommand.equals("loc")) {
                return null;
            }

            PlayerSettings p = plugin.getPlayerSettings(player);
            Dungeon d = p.getCurrentDungeon();

            if (d == null) {
                return null;
            }

            return d.getLocationGroupings().stream().filter(groupingName -> groupingName.toUpperCase().startsWith(input))
                    .collect(Collectors.toList());

        }

        return null;
    }

}
