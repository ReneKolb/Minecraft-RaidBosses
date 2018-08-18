package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public class HungerPerSecLine implements ITooltipLine {

    private int healAmount;

    private int tickDelay;

    public HungerPerSecLine(int healAmount, int tickDelay) {
        this.healAmount = healAmount;
        this.tickDelay = tickDelay;
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.HUNGER + "Hunger", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = ChatColor.GREEN + "+" + ChatColor.WHITE + healAmount + Colors.PER_SECOND + " per "
                + SkillTooltipBuilder.formatTimeSec(tickDelay);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
