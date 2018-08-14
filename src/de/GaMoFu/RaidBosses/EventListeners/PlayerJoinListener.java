package de.GaMoFu.RaidBosses.EventListeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.GaMoFu.RaidBosses.RaidBosses;

public class PlayerJoinListener implements Listener {

	private RaidBosses plugin;

	public PlayerJoinListener(RaidBosses plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		World world = event.getPlayer().getLocation().getWorld();

		event.getPlayer().teleport(world.getSpawnLocation());

		this.plugin.addNewPlayerSettings(event.getPlayer());
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		// This method must be called the last since it will actually remove the player settings
		this.plugin.removePlayerSettings(event.getPlayer());
	}

}
