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
        String left = FontUtil.formatStringToWidth(Colors.HUNGER + "Hunger", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = ChatColor.RED + "-" + ChatColor.WHITE + hungerCost;

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
