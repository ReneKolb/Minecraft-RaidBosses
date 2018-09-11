package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

public class DescriptionLine implements ITooltipLine {

    private String totalDescription;

    public DescriptionLine(String totalDescription) {
        this.totalDescription = totalDescription;
    }

    @Override
    public List<String> formatLine() {
        String[] lines = WordUtils.wrap(this.totalDescription, SkillTooltipBuilder.MAX_LINE_LENGTH, "\n", true)
                .split("\\n");

        List<String> result = new LinkedList<>();
        for (String line : lines) {
            result.add(Colors.DESCRIPTION + line);
        }

        return result;
    }

}
