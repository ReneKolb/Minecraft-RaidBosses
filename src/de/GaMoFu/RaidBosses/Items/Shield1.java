package de.GaMoFu.RaidBosses.Items;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Attributes.Attributes.AttributeType;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Slot;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class Shield1 extends Item {

    public static final String INTERNAL_NAME = "BASIC_SHIELD";

    @Override
    public ItemTier getItemTier() {
        return ItemTier.TIER_1;
    }

    @Override
    protected String getItemDisplayNameWithoutColor() {
        return "Basic Shield";
    }

    @Override
    public String getItemInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.SHIELD;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("A Shield for basic protection"))
                .add(new EmptyLine());
        //@formatter:on
    }

    @Override
    public List<Attribute> getAttributes() {
        List<Attribute> result = new LinkedList<>();

        result.add(Attribute.newBuilder().type(AttributeType.GENERIC_MAX_HEALTH).name("health").amount(2)
                .slot(Slot.OFFHAND).build());

        return result;
    }

}
