package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

public class HorizontalLine implements ITooltipLine {

    private ChatColor color;

    public HorizontalLine(ChatColor color) {
        this.color = color;
    }
    
    public HorizontalLine() {
        this.color = ChatColor.GRAY;
    }

    @Override
    public List<String> formatLine() {
        return Arrays.asList(color + StringUtils.repeat("-", SkillTooltipBuilder.MAX_LINE_LENGTH));
    }

}
