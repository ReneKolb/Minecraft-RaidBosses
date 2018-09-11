package de.GaMoFu.RaidBosses.Commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.EventListeners.DungeonDesignListener;

public class CommandManager {

    private WorldCommand worldCommand;
    private TestCommand testCommand;
    private DungeonControlCommands dungeonControlCommands;
    private SkillCommands skillCommands;
    private ItemCommands itemCommands;
    private TraderCommands traderCommands;
    private ShowCommands showCommands;

    public CommandManager(RaidBosses plugin) {
        this.worldCommand = new WorldCommand();
        plugin.getCommand("world").setTabCompleter(new WorldTabComplete(plugin));

        this.testCommand = new TestCommand();
        this.dungeonControlCommands = new DungeonControlCommands();
        plugin.getCommand("set").setTabCompleter(new DungeonControlTabComplete(plugin));
        plugin.getCommand("add").setTabCompleter(new DungeonControlTabComplete(plugin));
        plugin.getCommand("del").setTabCompleter(new DungeonControlTabComplete(plugin));

        this.skillCommands = new SkillCommands();
        plugin.getCommand("skill").setTabCompleter(new SkillTabComplete(plugin));

        this.itemCommands = new ItemCommands();
        plugin.getCommand("item").setTabCompleter(new ItemTabComplete(plugin));

        this.traderCommands = new TraderCommands();
        plugin.getCommand("trader").setTabCompleter(new TraderTabComplete(plugin));

        this.showCommands = new ShowCommands();
        plugin.getCommand("show").setTabCompleter(new ShowTabCompleter(plugin));
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

        if (command.getName().equalsIgnoreCase("trader")) {
            return this.traderCommands.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("show")) {
            return this.showCommands.handleCommand(plugin, sender, command, label, args);
        }

        if (command.getName().equalsIgnoreCase("/mw")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can execute this command");
                return true;
            }
            Player player = (Player) sender;

            player.getInventory().addItem(DungeonDesignListener.buildMagicWand());
            return true;
        }

        if (command.getName().equalsIgnoreCase("nv")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can execute this command");
                return true;
            }
            Player player = (Player) sender;

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                sender.sendMessage("Only in Creative Mode");
                return true;
            }

            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            } else {
                player.addPotionEffect(
                        new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 100, false, false), true);
            }
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

        sender.sendMessage(ChatColor.RED + "Command is not handled!");
        return false;
    }

}
