package de.GaMoFu.RaidBosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HologramHandler {

    private static final int DAMAGE_SHOW_TICKS = 20 * 2;

    private boolean isEnabled;

    private RaidBosses plugin;

    public HologramHandler(RaidBosses plugin) {
        this.plugin = plugin;

        init();
    }

    public void init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            plugin.getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            plugin.getLogger().severe("*** Damage indications and speech bubbles will not be displayed. ***");

            this.isEnabled = false;
            return;
        }

        this.isEnabled = true;
    }

    public void showHologram(Location loc, String text, int durationTicks, Player player) {
        if (!isEnabled)
            return;

        final Hologram hologram = HologramsAPI.createHologram(this.plugin, loc);
        if (player != null) {
            hologram.getVisibilityManager().setVisibleByDefault(false);
            hologram.getVisibilityManager().showTo(player);
        }

        hologram.appendTextLine(text);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                hologram.delete();
            }

        }, durationTicks);

    }

    public boolean isHologramEntity(Entity entity) {
        if (!this.isEnabled)
            return false;

        return HologramsAPI.isHologramEntity(entity);
    }

    private ChatColor getColorFromDamageCause(DamageCause cause) {
        // System.out.println("Cause: "+cause);
        switch (cause) {
        case CUSTOM:
            return ChatColor.STRIKETHROUGH;
        case ENTITY_ATTACK:
            // normal melee attack
            return ChatColor.WHITE;
        case ENTITY_EXPLOSION:
        case ENTITY_SWEEP_ATTACK:
            return ChatColor.AQUA;
        case FIRE:
        case FIRE_TICK:
            return ChatColor.GOLD;
        case LIGHTNING:
            return ChatColor.YELLOW;
        case MAGIC:
            return ChatColor.DARK_PURPLE;
        case POISON:
            return ChatColor.GREEN;
        case PROJECTILE:
            return ChatColor.WHITE;
        case THORNS:
            return ChatColor.BLUE;
        case WITHER:
            return ChatColor.GRAY;
        }
        return ChatColor.WHITE;
    }

    public void displayDamage(Player damager, Entity target, double damage, DamageCause cause) {

        Vector dir = damager.getLocation().subtract(target.getLocation()).toVector();
        dir = dir.normalize();
        dir = dir.multiply(target.getWidth() * 1.15);

        Location loc = target.getLocation().add(dir).add(0, damager.getEyeHeight(), 0);

        showHologram(loc, getColorFromDamageCause(cause) + "" + Math.round(damage), DAMAGE_SHOW_TICKS, damager);

    }

}
