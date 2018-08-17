package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;

public class SkillTooltipBuilder {

    public static final int MAX_LINE_LENGTH = 32;

    public static final String TABLE_FORMAT_PATTERN = "%-11s" + ChatColor.GRAY + ": %s";

    private List<ITooltipLine> lines;

    public SkillTooltipBuilder() {
        this.lines = new LinkedList<>();
    }

    public SkillTooltipBuilder add(ITooltipLine line) {
        lines.add(line);
        return this;
    }

    public List<String> build() {
        List<String> result = new LinkedList<>();
        for (ITooltipLine line : lines) {
            result.addAll(line.formatLine());
        }
        return result;
    }

}
