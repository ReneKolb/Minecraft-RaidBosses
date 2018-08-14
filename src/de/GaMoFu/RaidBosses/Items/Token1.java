package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

public class Token1 extends Token {

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_1;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_NUGGET;
    }

}
