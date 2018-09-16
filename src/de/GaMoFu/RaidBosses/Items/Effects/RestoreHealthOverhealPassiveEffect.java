package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class RestoreHealthOverhealPassiveEffect extends ItemEffect {

    public static final RestoreHealthOverhealPassiveEffect INSTANCE = new RestoreHealthOverhealPassiveEffect();

    private RestoreHealthOverhealPassiveEffect() {

    }

    @Override
    public String getTootipText() {
        return  "Restore " + SkillTooltipBuilder.formatHealth(1) + ChatColor.YELLOW
                + " every 2 second. Overheal will heal the lowest group member within 10m.";
    }

    @Override
    public int getTickDelay() {
        return 40;
    }

    @Override
    public void onTick(Player player) {
        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (player.getHealth() < playerMaxHealth) {
            player.setHealth(Math.min(playerMaxHealth, player.getHealth() + 1));

            Bukkit.getServer().getPluginManager().callEvent(new EntityRegainHealthEvent(player, 1, RegainReason.MAGIC));
            return;
        }

        // Handle overheal: share in group and heal lowest player within 10m
        double lowestHealthPercent = 1.0;
        Player lowestPlayer = null;

        for (Player p : player.getWorld().getPlayers()) {
            if (p.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }

            if (p.getLocation().distanceSquared(player.getLocation()) > 100) {
                continue;
            }

            double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double healthPercent = p.getHealth() / maxHealth;

            if (healthPercent < lowestHealthPercent) {
                lowestHealthPercent = healthPercent;
                lowestPlayer = p;
            }

        }

        if (lowestPlayer != null) {
            double maxHealth = lowestPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (lowestPlayer.getHealth() < maxHealth) {
                lowestPlayer.setHealth(Math.min(maxHealth, lowestPlayer.getHealth() + 1));
                Bukkit.getServer().getPluginManager()
                        .callEvent(new EntityRegainHealthEvent(lowestPlayer, 1, RegainReason.MAGIC));
            }
        }

    }

    @Override
    public String getUniqueEffectID() {
        return "30e5b375-c76e-4efa-81c2-7a81f39c1c5c";
    }
}