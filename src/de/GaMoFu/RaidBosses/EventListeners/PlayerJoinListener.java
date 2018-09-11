package de.GaMoFu.RaidBosses.EventListeners;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.RaidBosses;

public class PlayerJoinListener implements Listener {

    private RaidBosses plugin;

    public PlayerJoinListener(RaidBosses plugin) {
        this.plugin = plugin;
    }

    // Call this event with low priority, so the playerSettings is added prior to
    // other eventHandlers
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.addNewPlayerSettings(event.getPlayer());

        World world = event.getPlayer().getLocation().getWorld();

        event.getPlayer().teleport(world.getSpawnLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // This method must be called the last since it will actually remove the player
        // settings
        this.plugin.removePlayerSettings(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        if (!event.getNewGameMode().equals(GameMode.CREATIVE)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

    }
}
