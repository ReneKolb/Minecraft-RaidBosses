package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.GaMoFu.RaidBosses.RaidBosses;

public class RestoreHungerOnHitEffect extends ItemEffect {

    public static final RestoreHungerOnHitEffect INSTANCE = new RestoreHungerOnHitEffect();

    private RestoreHungerOnHitEffect() {

    }

    @Override
    public String getTootipText() {
        return ChatColor.YELLOW + "Hitting an enemy has a chance of 40% of restoring 3 Hunger";
    }

    @Override
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (RaidBosses.random.nextDouble() > 0.4) {
            return;
        }

        Player damager = (Player) event.getDamager();

        damager.setFoodLevel(Math.min(20, damager.getFoodLevel() + 3));
    }

}
