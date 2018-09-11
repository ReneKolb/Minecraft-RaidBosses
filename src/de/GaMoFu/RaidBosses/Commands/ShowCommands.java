package de.GaMoFu.RaidBosses.Commands;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Dungeons.Dungeon;

public class ShowCommands implements ICommandHandler {

    @Override
    public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a Player");
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        Player player = (Player) sender;

        PlayerSettings p = plugin.getPlayerSettings(player);

        if (args[0].equalsIgnoreCase("loc")) {

            Dungeon d = p.getCurrentDungeon();
            if (d == null) {
                player.sendMessage("You have to be in a Dungeon");
                return true;
            }

            Map<String, Location> locs = d.getLocationGroupings(args[1]);
            if (locs == null) {
                player.sendMessage("Group not found");
                return true;
            }

            for (Location loc : locs.values()) {
                Block block = loc.getBlock();
                block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().clone().add(0.5, 0.5, 0.5),
                        5, 0.3d, 0.15d, 0.3d);
                block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().clone().add(0.5, 0.5, 0.5),
                        14, 0.4, 0.4, 0.4);
            }
            return true;

        }

        return false;
    }

}
