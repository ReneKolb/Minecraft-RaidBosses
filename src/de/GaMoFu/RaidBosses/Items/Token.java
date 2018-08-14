package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;

public abstract class Token implements IItem {

	public abstract int getLevel();

	public abstract ItemTier getItemTier();

	@Override
	public List<Attribute> getAttributes() {
		return null;
	}

	@Override
	public String getItemDisplayName() {
		return getItemTier().getDisplayColor() + "Token Lv. " + getLevel();
	}

	@Override
	public String getItemInternalName() {
		return "TOKEN_LVL_" + getLevel();
	}

	@Override
	public List<String> getLore() {
		return Arrays
				.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Trade this token for a Lv." + getLevel() + " item");
	}

	@Override
	public void onDamageEntity(EntityDamageByEntityEvent event) {
	}

}
