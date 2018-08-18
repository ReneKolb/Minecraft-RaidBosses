package de.GaMoFu.RaidBosses.Items;

import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class Token extends Item {

    public abstract int getLevel();

    @Override
    public String getItemDisplayNameWithoutColor() {
        return "Token Lv. " + getLevel();
    }

    @Override
    public String getItemInternalName() {
        return "TOKEN_LVL_" + getLevel();
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("Trade this token for a Tier" + getLevel() + " item"))
                .add(new EmptyLine());
        //@formatter:on
    }

}
