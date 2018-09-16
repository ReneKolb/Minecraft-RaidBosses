package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Attributes.Attributes.AttributeType;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Slot;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.LightningEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Excalibur extends Item {

    public static final String INTERNAL_NAME = "EXCALIBUR";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_RAINBOW;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Excalibur";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("The sword to rule them all!"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<Attribute> getAttributes() {
        List<Attribute> result = new LinkedList<>();

        result.add(Attribute.newBuilder().type(AttributeType.GENERIC_ATTACK_DAMAGE).name("damage").amount(25)
                .slot(Slot.MAINHAND).build());

        result.add(Attribute.newBuilder().type(AttributeType.GENERIC_MAX_HEALTH).name("health").amount(5)
                .slot(Slot.MAINHAND).build());

        return result;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return Arrays.asList(LightningEffect.INSTANCE);
    }

}
