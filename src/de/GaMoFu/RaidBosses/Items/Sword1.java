package de.GaMoFu.RaidBosses.Items;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Sword1 extends Item {

    public static final String INTERNAL_NAME = "BASIC_SWORD";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_1;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Basic Sword";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.STONE_SWORD;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A basic sword"))
                .add(new EmptyLine());
        //@formatter:on
    }

}
