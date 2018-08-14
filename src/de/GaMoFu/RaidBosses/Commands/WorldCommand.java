package de.GaMoFu.RaidBosses.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Utils;

public class WorldCommand implements ICommandHandler {

	public static final String WORLD_NAME_PREFIX = "instance_";

	@Override
	public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("create")) {
			if (args.length != 2) {
				sender.sendMessage("You can only specify one world");
				return false;
			}

			String worldName = WORLD_NAME_PREFIX + args[1];

			if (plugin.getWorlds().worldExists(worldName)) {
				sender.sendMessage("Cannot create '" + ChatColor.BLUE + worldName + ChatColor.RESET + "'. The world already exists.");
				return true;
			}

			if (plugin.getWorlds().createWorld(worldName)) {
				sender.sendMessage("Created world: '" + ChatColor.BLUE + worldName + ChatColor.RESET + "'.");
			} else {
				sender.sendMessage(ChatColor.RED + "An error occured while creating world: '" + ChatColor.BLUE + worldName + ChatColor.RED + "'");
			}

			return true;
		} else if (args.length >= 1 && (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))) {
			if (args.length != 2) {
				sender.sendMessage("You can only specify one world");
				return false;
			}

			if (sender instanceof Player) {
				Player player = (Player) sender;

				if (args[1].equalsIgnoreCase("home")) {
					Location target = plugin.getServer().getWorlds().get(0).getSpawnLocation();
					player.teleport(target);
					return true;
				}

				String worldName;
				if (args[1].startsWith(WORLD_NAME_PREFIX)) {
					worldName = args[1];
				} else {
					worldName = WORLD_NAME_PREFIX + args[1];
				}

				World world = plugin.getWorlds().getWorld(worldName);

				if (world == null) {
					sender.sendMessage("World '" + ChatColor.BLUE + worldName + ChatColor.RESET + "' not found");
					return true;
				}

				Location target = world.getSpawnLocation();
				if (target == null) {
					sender.sendMessage("No spawn location set for world '" + ChatColor.BLUE + worldName + ChatColor.RESET + "'");
					target = world.getHighestBlockAt(0, 0).getLocation();
					if (target == null) {
						sender.sendMessage("No higghest block at (0,0). Create one");
						target = new Location(world, 0, 1, 0);
						world.getBlockAt(target.clone().subtract(0, 1, 0)).setType(Material.STONE);
					}
				}

				player.teleport(target);

				return true;
			} else {
				sender.sendMessage("You have to be a player (for now");
				return false;
			}

		} else if (args.length >= 1 && args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage("Unsupported for now");
		} else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(StringUtils.join(plugin.getWorlds().getWorlds(), ", "));

			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("spawnpoint")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;

				World world = player.getWorld();
				Location newSpawnPoint = player.getLocation();
				world.setSpawnLocation(newSpawnPoint);

				String spawnString = Utils.LocationToString(newSpawnPoint);

				player.sendMessage("Set the the world's ('" + ChatColor.BLUE + world.getName() + ChatColor.RESET + "') spawnpoint to " + spawnString);
			}
			return true;

		}
		return false;
	}

}
