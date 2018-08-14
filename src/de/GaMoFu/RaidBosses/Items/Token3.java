package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token3 extends Token {

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_3;
    }
    
    @Override
    public Material getDisplayMaterial() {
        return Material.PRISMARINE_SHARD;
    }

}
