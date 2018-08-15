package de.GaMoFu.RaidBosses.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.EventListeners.DungeonDesignListener;

public class CommandManager {

    private WorldCommand worldCommand;
    private TestCommand testCommand;
    private DungeonControlCommands dungeonControlCommands;
    private SkillCommands skillCommands;
    private ItemCommands itemCommands;

    public CommandManager(RaidBosses plugin) {
        this.worldCommand = new WorldCommand();
        this.testCommand = new TestCommand();
        this.dungeonControlCommands = new DungeonControlCommands();
        this.skillCommands = new SkillCommands();

        this.itemCommands = new ItemCommands();
        plugin.getCommand("item").setTabCompleter(new ItemTabComplete(plugin));
    }

    public boolean onCommand(RaidBosses plugin, CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("world")) {
            return this.worldCommand.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("test")) {
            return this.testCommand.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("add")) {
            return this.dungeonControlCommands.handleCommandAdd(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("del")) {
            return this.dungeonControlCommands.handleCommandDelete(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("set")) {
            return this.dungeonControlCommands.handleCommandSet(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("skill")) {
            return this.skillCommands.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("item")) {
            return this.itemCommands.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("/mw")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can execute this command");
            }
            Player player = (Player) sender;

            player.getInventory().addItem(DungeonDesignListener.buildMagicWand());
            return true;
        }

        if (command.getName().equalsIgnoreCase("gm")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can execute this command");
            }

            if (args.length != 1)
                return false;

            Player player = (Player) sender;

            try {
                int i = Integer.parseInt(args[0]);
                GameMode gm = GameMode.getByValue(i);
                player.setGameMode(gm);
                return true;
            } catch (Exception e) {
                try {
                    GameMode gm = GameMode.valueOf(args[0].toUpperCase());
                    player.setGameMode(gm);
                } catch (Exception e2) {
                    player.sendMessage("Cannot find GameMode: " + args[0]);
                    return true;
                }
            }
            return true;

        }

        return false;
    }

}
