package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class RangeLine implements ITooltipLine {

    private double range;

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.RANGE + "Range", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatDistance(range);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
