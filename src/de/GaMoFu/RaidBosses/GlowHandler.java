package de.GaMoFu.RaidBosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

public class GlowHandler {

    private boolean isEnabled;

    private RaidBosses plugin;

    public GlowHandler(RaidBosses plugin) {
        this.plugin = plugin;

        init();
    }

    public void init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("GlowAPI")) {
            plugin.getLogger().severe("*** GlowAPI is not installed or not enabled. ***");
            plugin.getLogger().severe("*** Monsters will not have a colored glow. ***");

            this.isEnabled = false;
            return;
        }

        this.isEnabled = true;
    }

    public void showGlow(Entity entity, ChatColor color) {
        Color col = convertColor(color);

        if (isEnabled && col != null) {
            GlowAPI.setGlowing(entity, col, entity.getWorld().getPlayers());
        } else {
            entity.setGlowing(true);
        }
    }

    public void removeGlow(Entity entity) {
        if (isEnabled) {
            GlowAPI.setGlowing(entity, null, plugin.getServer().getOnlinePlayers());
        } else {
            entity.setGlowing(false);
        }
    }

    private static Color convertColor(ChatColor color) {
        switch (color) {
        case AQUA:
            return Color.AQUA;
        case BLACK:
            return Color.BLACK;
        case BLUE:
            return Color.BLUE;
        case DARK_AQUA:
            return Color.DARK_AQUA;
        case DARK_BLUE:
            return Color.DARK_BLUE;
        case DARK_GRAY:
            return Color.DARK_GRAY;
        case DARK_GREEN:
            return Color.DARK_GREEN;
        case DARK_PURPLE:
            return Color.DARK_PURPLE;
        case DARK_RED:
            return Color.DARK_RED;
        case GOLD:
            return Color.GOLD;
        case GRAY:
            return Color.GRAY;
        case GREEN:
            return Color.GREEN;
        // case LIGHT_PURPLE:
        case RED:
            return Color.RED;
        case WHITE:
            return Color.WHITE;
        case YELLOW:
            return Color.YELLOW;
        default:
            return null;
        }

    }

}
