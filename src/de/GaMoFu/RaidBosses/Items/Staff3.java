package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.RestoreHealthOverhealPassiveEffect;
import de.GaMoFu.RaidBosses.Items.Effects.RestoreHungerOverhealPassiveEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Staff3 extends Item {

    public static final String INTERNAL_NAME = "PROFESSIONAL_STAFF";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_3;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Professional Staff";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A mage's professional staff"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return Arrays.asList(RestoreHungerOverhealPassiveEffect.INSTANCE, RestoreHealthOverhealPassiveEffect.INSTANCE);
    }

    @Override
    public String getNMSTagForTexturepackModel() {
        return "Angelic Fork";
    }

}
