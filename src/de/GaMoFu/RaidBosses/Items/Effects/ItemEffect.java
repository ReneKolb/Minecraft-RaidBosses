package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class ItemEffect {

    public String getTootipText() {
        return null;
    }

    public void onDamageEntity(EntityDamageByEntityEvent event) {

    }
    
    public int getTickDelay() {
        return -1;
    }
    
    public void onTick(Player player) {
        
    }

}
