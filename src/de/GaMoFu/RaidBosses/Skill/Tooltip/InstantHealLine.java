package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class InstantHealLine implements ITooltipLine {

    private double healAmount;

    public InstantHealLine(double healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.HEALTH + "Heal", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatHealth(healAmount);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
