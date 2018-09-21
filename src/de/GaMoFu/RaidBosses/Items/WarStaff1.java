package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.UseSkillEffekt;
import de.GaMoFu.RaidBosses.Skill.Fireball1;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class WarStaff1 extends Item {

    public static final String INTERNAL_NAME = "WAR_STAFF";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_1;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "War Staff";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.WOODEN_AXE;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A mage's war staff"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        ISkill skill = RaidBosses.getPluginInstance().getSkillFactory()
                .getSkillFromInternalName(Fireball1.INTERNAL_NAME);

        return Arrays.asList(new UseSkillEffekt(skill));
    }

    @Override
    public String getNMSTagForTexturepackModel() {
        return "Angelic Fork";
    }

}
