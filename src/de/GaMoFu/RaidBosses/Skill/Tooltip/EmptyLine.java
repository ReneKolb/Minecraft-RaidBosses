package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class EmptyLine implements ITooltipLine {

    @Override
    public List<String> formatLine() {
        return Arrays.asList("");
    }

}
