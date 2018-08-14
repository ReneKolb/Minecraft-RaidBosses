package de.GaMoFu.RaidBosses.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.GaMoFu.RaidBosses.RaidBosses;

public interface ICommandHandler {

    public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label, String[] args);

}
