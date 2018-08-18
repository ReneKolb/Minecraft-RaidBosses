package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class HealPerSecLine implements ITooltipLine {

    private double healAmount;

    private int tickDelay;

    public HealPerSecLine(double healAmount, int tickDelay) {
        this.healAmount = healAmount;
        this.tickDelay = tickDelay;
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.HEALTH + "Heal", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatHealth(healAmount) + Colors.PER_SECOND + " per "
                + SkillTooltipBuilder.formatTimeSec(tickDelay);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
