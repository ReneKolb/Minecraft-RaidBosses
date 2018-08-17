package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public class HungerCostLine implements ITooltipLine {

    private int hungerCost;

    public HungerCostLine(int hungerCost) {
        this.hungerCost = hungerCost;
    }

    @Override
    public List<String> formatLine() {
        return Arrays
                .asList(ChatColor.DARK_PURPLE + "Hunger" + ChatColor.GRAY + " cost: " + ChatColor.WHITE + hungerCost);
    }

}
