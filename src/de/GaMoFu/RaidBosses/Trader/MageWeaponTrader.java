package de.GaMoFu.RaidBosses.Trader;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.MerchantRecipe;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Items.Staff1;
import de.GaMoFu.RaidBosses.Items.Staff2;
import de.GaMoFu.RaidBosses.Items.Staff3;

public class MageWeaponTrader extends Trader {
    // Level 1 Trader sells Items for Lv.1 Tokens

    public MageWeaponTrader() {
        super(RaidBosses.getPluginInstance());
    }

    @Override
    public String getDisplayName() {
        return "Mage Weapon Trainer";
    }

    @Override
    protected List<MerchantRecipe> getRecipes() {
        List<MerchantRecipe> result = new LinkedList<>();

        result.add(this.buildRecipe(plugin.getItemsFactory().buildItemItem(Staff1.INTERNAL_NAME),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_1", 5), null).get());

        result.add(this.buildRecipe(plugin.getItemsFactory().buildItemItem(Staff2.INTERNAL_NAME),
                plugin.getItemsFactory().buildItemItem(Staff1.INTERNAL_NAME),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_2", 5)).get());

        result.add(this.buildRecipe(plugin.getItemsFactory().buildItemItem(Staff3.INTERNAL_NAME),
                plugin.getItemsFactory().buildItemItem(Staff2.INTERNAL_NAME),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_3", 5)).get());

        return result;
    }

    @Override
    public String getInternalName() {
        return "MAGE_WEAPON_TRADER";
    }

}
