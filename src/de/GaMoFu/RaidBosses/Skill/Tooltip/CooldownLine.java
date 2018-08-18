package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class CooldownLine implements ITooltipLine {

    private int cooldownTicks;

    public CooldownLine(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;

    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.COOLDOWN + "Cooldown",
                SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatTimeSec(cooldownTicks);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
