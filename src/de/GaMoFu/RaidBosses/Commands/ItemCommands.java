package de.GaMoFu.RaidBosses.Commands;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.GaMoFu.RaidBosses.RaidBosses;

public class ItemCommands implements ICommandHandler {

	@Override
	public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed as a Player. (for now)");
			return true;
		}

		if (args.length < 2) {
			return false;
		}

		Player player = (Player) sender;

		if (args[0].equalsIgnoreCase("give")) {
			Optional<ItemStack> item;

			if (args.length == 2)
				item = plugin.getItemsFactory().buildItemItem(args[1]);
			else
				item = plugin.getItemsFactory().buildItemItem(args[1], Integer.parseInt(args[2]));

			if (!item.isPresent()) {
				player.sendMessage("Item '" + args[1] + "' not found");
			} else {
				player.getInventory().addItem(item.get());
			}

			return true;
		}

		return false;
	}
}
