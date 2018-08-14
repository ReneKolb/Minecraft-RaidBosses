package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token7 extends Token {

    @Override
    public int getLevel() {
        return 7;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_7;
    }
    
    @Override
    public Material getDisplayMaterial() {
        return Material.DIAMOND;
    }

}
