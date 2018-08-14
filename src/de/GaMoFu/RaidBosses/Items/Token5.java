package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token5 extends Token {

    @Override
    public int getLevel() {
        return 5;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_5;
    }
    
    @Override
    public Material getDisplayMaterial() {
        return Material.GOLD_NUGGET;
    }

}
