package de.GaMoFu.RaidBosses.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Attributes.Attributes;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;

public class ItemsFactory {

	private RaidBosses plugin;

	/** For looking up an item by its internal name */
	private Map<String, IItem> itemNameLookup; // SkillName -> Item

	/** For looking up an item be an held item's display name */
	private Map<String, IItem> itemDisplayNameLookup; // DisplayString -> Item

	public ItemsFactory(RaidBosses plugin) {
		this.plugin = plugin;
		this.itemNameLookup = new HashMap<>();
		this.itemDisplayNameLookup = new HashMap<>();

		init();
	}

	private void init() {
		this.plugin.getLogger().info("Initializing items");

		for (ItemsEnum i : ItemsEnum.values()) {
			Class<? extends IItem> itemClass = i.getItemClass();
			IItem item;
			try {
				item = itemClass.newInstance();

				this.itemNameLookup.put(item.getItemInternalName(), item);
				this.itemDisplayNameLookup.put(item.getItemDisplayName(), item);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public Optional<IItem> getItemFromDisplayName(String itemDisplayName) {
		if (this.itemDisplayNameLookup.containsKey(itemDisplayName))
			return Optional.of(this.itemDisplayNameLookup.get(itemDisplayName));

		return Optional.empty();
	}

	public Optional<ItemStack> buildItemItem(String itemName) {
		return buildItemItem(itemName, 1);
	}

	public Optional<ItemStack> buildItemItem(String itemName, int amount) {
		if (amount > 64)
			amount = 64;

		IItem item = itemNameLookup.get(itemName);
		if (item == null) {
			return Optional.empty();
		}

		ItemStack result = new ItemStack(item.getDisplayMaterial(), amount, item.getDisplayDurability());

		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(item.getItemDisplayName());
		meta.setLore(item.getLore());

		result.setItemMeta(meta);

		if (item.getAttributes() != null) {
			Attributes resultWithAttributes = new Attributes(result);
			for (Attribute att : item.getAttributes()) {
				resultWithAttributes.add(att);
			}

			result = resultWithAttributes.getStack();
		}

		return Optional.of(result);
	}
}
