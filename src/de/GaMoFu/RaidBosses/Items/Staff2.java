package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.RestoreHealthPassiveEffect;
import de.GaMoFu.RaidBosses.Items.Effects.RestoreHungerPassiveEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Staff2 extends Item {

    public static final String INTERNAL_NAME = "ADVANCED_STAFF";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_2;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Advanced Staff";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.STONE_AXE;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A mage's advanced staff"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return Arrays.asList(RestoreHungerPassiveEffect.INSTANCE, RestoreHealthPassiveEffect.INSTANCE);
    }

    @Override
    public String getNMSTagForTexturepackModel() {
        return "Angelic Fork";
    }

}
