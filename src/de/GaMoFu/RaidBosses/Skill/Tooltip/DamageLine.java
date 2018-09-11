package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.List;

public class DamageLine implements ITooltipLine {

    private double damage;

    public DamageLine(double damage) {
        this.damage = damage;

    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.DAMAGE + "Damage", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = SkillTooltipBuilder.formatDamage(damage);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }
}
