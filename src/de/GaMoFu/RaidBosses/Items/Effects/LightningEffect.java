package de.GaMoFu.RaidBosses.Items.Effects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class LightningEffect extends ItemEffect implements Listener {

    public static final LightningEffect INSTANCE = new LightningEffect();

    public static double DAMAGE_AMOUNT = 6.0;

    private Map<UUID, LightningInfo> infoMap;

    private class LightningInfo {

        private double damage;

        private UUID shooter;

        public LightningInfo(double damage, UUID shooter) {
            this.damage = damage;
            this.shooter = shooter;
        }

        /**
         * @return the damage
         */
        public double getDamage() {
            return damage;
        }

        /**
         * @return the shooter
         */
        public UUID getShooter() {
            return shooter;
        }

    }

    private LightningEffect() {
        this.infoMap = new HashMap<>();
    }

    @Override
    public String getTootipText() {
        return ChatColor.YELLOW + "Strikes a bolt of lightning on hit " + ChatColor.GRAY + "("
                + SkillTooltipBuilder.formatDamage(DAMAGE_AMOUNT) + ChatColor.GRAY + ")";
    }

    @Override
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        World w = event.getEntity().getWorld();
        LightningStrike lightning = w.strikeLightning(event.getEntity().getLocation());

        this.infoMap.put(lightning.getUniqueId(), new LightningInfo(DAMAGE_AMOUNT, event.getDamager().getUniqueId()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!infoMap.containsKey(event.getDamager().getUniqueId()))
            return;

        LightningInfo info = infoMap.get(event.getDamager().getUniqueId());

        // The shooter should not hurt himself
        if (event.getEntity().getUniqueId().equals(info.shooter)) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(info.getDamage());
    }

}
