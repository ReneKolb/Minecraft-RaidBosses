package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token6 extends Token {

    @Override
    public int getLevel() {
        return 6;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_6;
    }
    
    @Override
    public Material getDisplayMaterial() {
        return Material.GOLD_INGOT;
    }

}
