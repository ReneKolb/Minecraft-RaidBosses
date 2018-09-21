package de.GaMoFu.RaidBosses.Items;

import org.bukkit.ChatColor;

public enum ItemTier {

    //@formatter:off
    // ChatColor.White is not allowed. The color will be removed in item's meta.getDisplayName so the lookups by DisplayName will fail, since the lookup expects the ColorCode but the ItemMeta does not contain it anymore.
    TIER_1(ChatColor.GREEN),
    TIER_2(ChatColor.BLUE),
    TIER_3(ChatColor.DARK_PURPLE),
    TIER_4(ChatColor.GOLD),
    TIER_5(ChatColor.YELLOW),
    TIER_6(ChatColor.AQUA),
    TIER_7(ChatColor.RED),
    
    TIER_RAINBOW(ChatColor.MAGIC);
    //@formatter:on

    private ChatColor itemColor;

    private ItemTier(ChatColor color) {
        this.itemColor = color;
    }

    public ChatColor getDisplayColor() {
        return this.itemColor;
    }

}
