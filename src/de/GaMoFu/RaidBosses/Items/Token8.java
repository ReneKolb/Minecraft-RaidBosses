package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token8 extends Token {

    @Override
    public int getLevel() {
        return 8;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_8;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.REDSTONE;
    }

}
