package de.GaMoFu.RaidBosses.Items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Attributes.Attributes;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;

public class ItemsFactory {

    private RaidBosses plugin;

    /** For looking up an item by its internal name */
    private Map<String, Item> itemNameLookup; // SkillName -> Item

    private List<String> internalItemNames;

    /** For looking up an item be an held item's display name */
    private Map<String, Item> itemDisplayNameLookup; // DisplayString -> Item

    public ItemsFactory(RaidBosses plugin) {
        this.plugin = plugin;
        this.itemNameLookup = new HashMap<>();
        this.itemDisplayNameLookup = new HashMap<>();

        init();
    }

    public List<String> getItemInternalNames() {
        if (internalItemNames == null) {
            internalItemNames = new LinkedList<>(itemNameLookup.keySet());
        }
        return internalItemNames;
    }

    private void init() {
        this.plugin.getLogger().info("Initializing items");

        for (ItemsEnum i : ItemsEnum.values()) {
            Class<? extends Item> itemClass = i.getItemClass();
            Item item;
            try {
                item = itemClass.newInstance();

                this.itemNameLookup.put(item.getItemInternalName(), item);
                this.itemDisplayNameLookup.put(item.getItemDisplayName(), item);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Optional<Item> getItemFromDisplayName(String itemDisplayName) {
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

        Item item = itemNameLookup.get(itemName);
        if (item == null) {
            return Optional.empty();
        }

        ItemStack result = new ItemStack(item.getDisplayMaterial(), amount, item.getDisplayDurability());

        ItemMeta meta = result.getItemMeta();

        meta.setDisplayName(item.getItemDisplayName());
        List<String> lore = new LinkedList<>(item.getLore());
        List<String> effectLore = new LinkedList<>();
        for (ItemEffect effect : item.getItemEffects()) {
            String effectText = effect.getTootipText();
            if (!StringUtils.isEmpty(effectText)) {
                effectLore.add(effectText);
            }
        }

        int longestLine = -1;
        for (String l : lore) {
            longestLine = Math.max(longestLine, l.length());
        }
        for (String l : effectLore) {
            longestLine = Math.max(longestLine, l.length());
        }

        if (!effectLore.isEmpty()) {
            lore.add(ChatColor.GRAY + StringUtils.repeat("-", longestLine));
            lore.addAll(effectLore);
        }

        meta.setLore(lore);

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
