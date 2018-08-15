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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // This method must be called the last since it will actually remove the player
        // settings
        this.plugin.removePlayerSettings(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 100, false, false), true);
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

    }
}
