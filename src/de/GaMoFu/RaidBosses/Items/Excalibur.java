package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Attributes.Attributes.AttributeType;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Slot;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Items.Effects.LightningEffect;

public class Excalibur extends Item {

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
        return "EXCALIBUR";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("The sword to rule them all!");
    }

    @Override
    public List<Attribute> getAttributes() {
        List<Attribute> result = new LinkedList<>();

        result.add(Attribute.newBuilder().type(AttributeType.GENERIC_ATTACK_DAMAGE).name("damage").amount(25)
                .slot(Slot.MAINHAND).build());

        return result;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return Arrays.asList(LightningEffect.INSTANCE);
    }

}
