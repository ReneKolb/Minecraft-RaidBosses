package de.GaMoFu.RaidBosses.Commands;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.GaMoFu.RaidBosses.RaidBosses;

public class SkillCommands implements ICommandHandler {

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

        if (args[0].equalsIgnoreCase("give")) {
            Optional<ItemStack> skillItem = plugin.getSkillFactory().buildSkillItem(args[1]);
            if (!skillItem.isPresent()) {
                player.sendMessage("Skill '" + args[1] + "' not found");
            } else {
                player.getInventory().addItem(skillItem.get());
            }

            return true;
        }

        return false;
    }

}
