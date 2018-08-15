package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LightningEffect extends ItemEffect {

    public static final LightningEffect INSTANCE = new LightningEffect();

    private LightningEffect() {

    }

    @Override
    public String getTootipText() {
        return ChatColor.YELLOW + "Strikes a bolt of lightning on hit";
    }

    @Override
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        World w = event.getEntity().getWorld();
        w.strikeLightning(event.getEntity().getLocation());
    }

}
