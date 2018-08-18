package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class DurationLine implements ITooltipLine {

    private int durationTicks;

    public DurationLine(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.DURATION + "Duration",
                SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatTimeSec(durationTicks);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
