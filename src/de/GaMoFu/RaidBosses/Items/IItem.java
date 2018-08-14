package de.GaMoFu.RaidBosses.Items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;

public interface IItem {

    public String getItemDisplayName();

    public String getItemInternalName();

    public Material getDisplayMaterial();

    public List<Attribute> getAttributes();

    public void onDamageEntity(EntityDamageByEntityEvent event);

    default public short getDisplayDurability() {
        return 0;
    }

    public List<String> getLore();
}
