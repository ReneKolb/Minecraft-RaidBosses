package de.GaMoFu.RaidBosses.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.PlayerSettings.BlockSelection;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.EventListeners.DungeonDesignListener;
import de.GaMoFu.RaidBosses.Trader.Trader;

public class TraderCommands implements ICommandHandler {

    @Override
    public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a Player. (for now)");
            return true;
        }

        if (args.length < 1) {
            return false;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2)
                return false;

            PlayerSettings ps = plugin.getPlayerSettings(player);

            BlockSelection selection = ps.getBlockSelection();
            if (selection == null) {
                player.sendMessage(
                        "You have to selected a Block with your " + DungeonDesignListener.MAGIC_WAND_ITEM_DISPLAY);
                return true;
            }

            Trader t = plugin.getTraderFactory().getTrader(args[1]);
            if (t == null) {
                player.sendMessage("Trader: " + args[1] + " not found.");
                return true;
            }

            t.spawn(selection.getBlock().getLocation().add(0, 1, 0));
            // TODO: add to global list (save to file) & spawn on next server restart

            return true;
        } else if (args[0].equalsIgnoreCase("del")) {
            if (args.length != 1)
                return false;

            PlayerSettings ps = plugin.getPlayerSettings(player);
            player.sendMessage("Not supported for now...");
            // MonsterSelection selection = ps.getTraderSelection();
            return true;
        }

        return false;
    }
}
