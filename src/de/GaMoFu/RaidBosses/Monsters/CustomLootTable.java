package de.GaMoFu.RaidBosses.Monsters;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.RaidBosses;

public class CustomLootTable implements LootTable {

    private int globalQuantityMin;

    private int globalQuantityMax;

    private List<LootItemSetting> saveDrop;

    private Map<Integer, LootItemSetting> internalMap;

    private int internalWeightSum;

    /**
     * 
     * @param globalQuantityMin
     *            minimum amount of random items. This amount specifies the amount
     *            of randomly added items. This amount is independent of the amount
     *            of added SaveDrops
     * @param globalQuantityMax
     */
    public CustomLootTable(int globalQuantityMin, int globalQuantityMax) {
        this.globalQuantityMin = globalQuantityMin;
        this.globalQuantityMax = globalQuantityMax;

        this.internalMap = new LinkedHashMap<>();
        this.saveDrop = new LinkedList<>();
    }

    public CustomLootTable add(String templateName, int stackSize, int weight) {
        LootItemSetting item = new LootItemSetting(templateName, stackSize, weight);

        internalWeightSum += weight;
        internalMap.put(internalWeightSum, item);

        return this;
    }

    public CustomLootTable addSaveDrop(String templateName, int stackSize) {
        LootItemSetting item = new LootItemSetting(templateName, stackSize, 0);
        saveDrop.add(item);

        return this;
    }

    private LootItemSetting getLootItemSettingForRandomValue(int value) {
        for (Integer val : internalMap.keySet()) {
            if (value <= val) {
                return internalMap.get(val);
            }
        }

        return null;
    }

    @Override
    public NamespacedKey getKey() {
        return null;
    }

    @Override
    public void fillInventory(Inventory inventory, Random rnd, LootContext lootContext) {

        inventory.clear();

        Collection<ItemStack> items = populateLoot(rnd, lootContext);

        for (ItemStack itemStack : items) {
            // TODO: maybe at random position??
            inventory.addItem(itemStack);
        }

    }

    @Override
    public Collection<ItemStack> populateLoot(Random rnd, LootContext lootContext) {
        if (this.internalMap.isEmpty() && this.saveDrop.isEmpty()) {
            return Collections.emptyList();
        }

        int amount = globalQuantityMin + rnd.nextInt(globalQuantityMax - globalQuantityMin + 1);
        if (this.internalMap.isEmpty()) {
            // if no random items specified, no items can be added
            amount = 0;
        }

        List<ItemStack> result = new LinkedList<>();

        // Add items that are dropped with a chance of 100%
        for (LootItemSetting saveItem : saveDrop) {
            Optional<ItemStack> itemStack = RaidBosses.getPluginInstance().getItemsFactory()
                    .buildItemItem(saveItem.getTemplateName(), saveItem.getStackSize());

            if (!itemStack.isPresent()) {
                System.err.println("Error while generating Loot. Item not found '" + saveItem.getTemplateName() + "'");
                continue;
            }

            result.add(itemStack.get());
        }

        // Now add other items randomly depending on their weight
        for (int i = 0; i < amount; i++) {
            int random = rnd.nextInt(internalWeightSum);
            LootItemSetting itemSetting = getLootItemSettingForRandomValue(random);

            if (itemSetting == null) {
                System.err.println("itemSetting not found for given random value");
                continue;
            }

            Optional<ItemStack> itemStack = RaidBosses.getPluginInstance().getItemsFactory()
                    .buildItemItem(itemSetting.getTemplateName(), itemSetting.getStackSize());

            if (!itemStack.isPresent()) {
                System.err
                        .println("Error while generating Loot. Item not found '" + itemSetting.getTemplateName() + "'");
                continue;
            }

            result.add(itemStack.get());
        }

        return result;
    }

}
