package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.RestoreHungerPassiveEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Staff1 extends Item {

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_1;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Basic Staff";
    }

    @Override
    public String getItemInternalName() {
        return "BASIC_STAFF";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_HOE;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A mage's staff"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return Arrays.asList(RestoreHungerPassiveEffect.INSTANCE);
    }

}
