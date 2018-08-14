package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token2 extends Token {

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_2;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_INGOT;
    }

}
