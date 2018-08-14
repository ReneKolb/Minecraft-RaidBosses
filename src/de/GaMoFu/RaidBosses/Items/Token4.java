package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token4 extends Token {

    @Override
    public int getLevel() {
        return 4;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_4;
    }
    
    @Override
    public Material getDisplayMaterial() {
        return Material.EMERALD;
    }

}
