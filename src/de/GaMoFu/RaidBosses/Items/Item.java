package de.GaMoFu.RaidBosses.Items;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;

public abstract class Item {

    public abstract ItemTier getItemTier();

    protected abstract String getItemDisplayNameWithoutColor();

    public String getItemDisplayName() {
        return getItemTier().getDisplayColor() + getItemDisplayNameWithoutColor();
    }

    public abstract String getItemInternalName();

    public abstract Material getDisplayMaterial();

    public List<Attribute> getAttributes() {
        return Collections.emptyList();
    }

    // public void onDamageEntity(EntityDamageByEntityEvent event);

    public List<ItemEffect> getItemEffects() {
        return Collections.emptyList();
    }

    public short getDisplayDurability() {
        return 0;
    }

    public List<String> getLore() {
        return Collections.emptyList();
    }
}
