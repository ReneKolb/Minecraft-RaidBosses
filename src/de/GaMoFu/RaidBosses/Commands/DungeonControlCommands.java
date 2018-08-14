package de.GaMoFu.RaidBosses.Commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.PlayerSettings.BlockSelection;
import de.GaMoFu.RaidBosses.PlayerSettings.MonsterSelection;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedBoss;
import de.GaMoFu.RaidBosses.EventListeners.DungeonDesignListener;
import de.GaMoFu.RaidBosses.Monsters.BossType;
import de.GaMoFu.RaidBosses.Monsters.MonsterType;

public class DungeonControlCommands {

    public boolean handleCommandAdd(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a player");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        Player player = (Player) sender;
        PlayerSettings ps = plugin.getPlayerSettings(player);

        BlockSelection selection = ps.getBlockSelection();
        if (selection == null) {
            player.sendMessage(
                    "You have to selected a Block with your " + DungeonDesignListener.MAGIC_WAND_ITEM_DISPLAY);
            return true;
        }

        Optional<MonsterType> type = MonsterType.fromAliasName(args[0]);
        if (type.isPresent()) {
            selection.getDungeon().addMonster(type.get(),
                    selection.getBlock().getLocation().clone().add(0.5, 1.0, 0.5));
        } else {
            Optional<BossType> bossType = BossType.fromAliasName(args[0]);
            if (bossType.isPresent()) {
                selection.getDungeon().addBoss(bossType.get(),
                        selection.getBlock().getLocation().clone().add(0.5, 1.0, 0.5));
            } else {
                sender.sendMessage("Monster or Boss Type not found.");
            }
        }

        return true;
    }

    public boolean handleCommandDelete(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a player");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        Player player = (Player) sender;
        PlayerSettings ps = plugin.getPlayerSettings(player);

        MonsterSelection selection = ps.getMonsterSelection();
        if (selection == null) {
            player.sendMessage(
                    "You have to select a monster with your " + DungeonDesignListener.MAGIC_WAND_ITEM_DISPLAY);
            return true;
        }

        selection.getDungeon().removeMonster(selection.getMonster());

        return true;
    }

    private void printSuccess(CommandSender sender, boolean success) {
        if (success) {
            sender.sendMessage(ChatColor.GREEN + "Success!");
        } else {
            sender.sendMessage(ChatColor.RED + "An error occured...");
        }
    }

    public boolean handleCommandSet(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a player");
            return true;
        }

        if (args.length < 1)
            return false;

        Player player = (Player) sender;
        PlayerSettings ps = plugin.getPlayerSettings(player);

        if (args[0].equalsIgnoreCase("lootchest")) {
            if (ps.getMonsterSelection() == null || !(ps.getMonsterSelection().getMonster() instanceof SpawnedBoss)) {
                player.sendMessage("You have to select a boss first with yout magic wand");
                return true;
            }

            ps.selectLootchest = true;
            return true;
        }

        if (args.length < 2) {
            return false;
        }

        MonsterSelection selection = ps.getMonsterSelection();
        if (selection == null) {
            player.sendMessage(
                    "You have to select a monster with your " + DungeonDesignListener.MAGIC_WAND_ITEM_DISPLAY);
            return true;
        }

        if (args[0].equalsIgnoreCase("stroll")) {
            if (args.length == 2) {
                // only one other argument is given. assume stroll radius
                boolean result = selection.getDungeon().updateStrollingRadius(selection.getMonster(),
                        Float.parseFloat(args[1]));
                printSuccess(sender, result);
                return true;
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("min")) {
                    boolean result = selection.getDungeon().updateStrollingMinDelay(selection.getMonster(),
                            Integer.parseInt(args[2]));
                    printSuccess(sender, result);
                } else if (args[1].equalsIgnoreCase("max")) {
                    boolean result = selection.getDungeon().updateStrollingMaxDelay(selection.getMonster(),
                            Integer.parseInt(args[2]));
                    printSuccess(sender, result);
                } else {
                    player.sendMessage("Unkown subcommant. Valid options are /set stroll [min|max] [value]");
                }
                return true;
            }
            return false;
        }

        if (args[0].equalsIgnoreCase("patrol")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("add")) {
                Location loc = player.getLocation();
                boolean result = selection.getDungeon().addPatrolWaypoint(selection.getMonster(), loc);
                printSuccess(sender, result);
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("show")) {
                selection.getDungeon().highlightPatrolPath(selection.getMonster());
                return true;
            }
        }

        return true;
    }

}
