package de.GaMoFu.RaidBosses.Items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Attributes.Attributes;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.ItemEffectLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class ItemsFactory {

    private static final String TEXTUREPACK_CUSTOM_MODEL_TAG_NAME = "modelname";

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

    public Optional<Item> getItemFromItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return Optional.empty();
        }

        String displayName = itemStack.getItemMeta().getDisplayName();
        return getItemFromDisplayName(displayName);
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

        result.addUnsafeEnchantments(item.getEnchantments());

        ItemMeta meta = result.getItemMeta();

        meta.setDisplayName(item.getItemDisplayName());
        SkillTooltipBuilder tooltipBuilder = item.getTooltipBuilder();
        if (tooltipBuilder == null) {
            tooltipBuilder = new SkillTooltipBuilder();
        }

        // if(!tooltipBuilder.isEmpty()&&!(tooltipBuilder.getLastLine()instanceof
        // HorizontalLine)) {
        // tooltipBuilder.add(new HorizontalLine());
        // }

        for (ItemEffect effect : item.getItemEffects()) {
            tooltipBuilder.add(new ItemEffectLine(effect));
        }

        meta.setLore(tooltipBuilder.build());

        if (item.isUnbreakable()) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        result.setItemMeta(meta);

        if (item.getAttributes() != null) {
            Attributes resultWithAttributes = new Attributes(result);
            for (Attribute att : item.getAttributes()) {
                resultWithAttributes.add(att);
            }

            result = resultWithAttributes.getStack();
        }

        if (item.getNMSTagForTexturepackModel() != null) {
            // Since the custom texture pack can define a custom model & texture (using
            // OptiFine / MCPatcher) for specific nms tags, this tag will be set here
            result = setTexturepackTag(result, item.getNMSTagForTexturepackModel());
        }

        item.postProcessItemStack(result);

        return Optional.of(result);
    }

    public static ItemStack setTexturepackTag(ItemStack item, String texturePackTag) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        if (nmsStack.getTag() == null) {
            nmsStack.setTag(new NBTTagCompound());
        }
        NBTTagCompound parentTag = nmsStack.getTag();

        parentTag.setString(TEXTUREPACK_CUSTOM_MODEL_TAG_NAME, texturePackTag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
