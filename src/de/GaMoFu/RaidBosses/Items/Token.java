package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public abstract class Token extends Item {

    public abstract int getLevel();

    @Override
    public String getItemDisplayNameWithoutColor() {
        return "Token Lv. " + getLevel();
    }

    @Override
    public String getItemInternalName() {
        return "TOKEN_LVL_" + getLevel();
    }

    @Override
    public List<String> getLore() {
        return Arrays
                .asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Trade this token for a Tier" + getLevel() + " item");
    }

}
