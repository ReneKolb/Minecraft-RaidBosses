package de.GaMoFu.RaidBosses.Items;

import org.bukkit.ChatColor;

public enum ItemTier {

    //@formatter:off
    TIER_1(ChatColor.WHITE),
    TIER_2(ChatColor.GREEN),
    TIER_3(ChatColor.BLUE),
    TIER_4(ChatColor.DARK_PURPLE),
    TIER_5(ChatColor.GOLD),
    TIER_6(ChatColor.YELLOW),
    TIER_7(ChatColor.AQUA),
    TIER_8(ChatColor.RED);
    //@formatter:on

    private ChatColor itemColor;

    private ItemTier(ChatColor color) {
        this.itemColor = color;
    }

    public ChatColor getDisplayColor() {
        return this.itemColor;
    }

}
