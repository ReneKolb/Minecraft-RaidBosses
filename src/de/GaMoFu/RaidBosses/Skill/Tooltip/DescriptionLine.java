package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

public class DescriptionLine implements ITooltipLine {

    private String totalDescription;

    public DescriptionLine(String totalDescription) {
        this.totalDescription = totalDescription;
    }

    @Override
    public List<String> formatLine() {
        return Arrays.asList(
                WordUtils.wrap(this.totalDescription, SkillTooltipBuilder.MAX_LINE_LENGTH, "\n", true).split("\\n"));
    }

}
