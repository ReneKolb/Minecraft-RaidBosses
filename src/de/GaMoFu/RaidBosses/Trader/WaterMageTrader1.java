package de.GaMoFu.RaidBosses.Trader;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.MerchantRecipe;

import de.GaMoFu.RaidBosses.RaidBosses;

public class WaterMageTrader1 extends Trader {
    // Level 1 Trader sells Items for Lv.1 Tokens

    public WaterMageTrader1() {
        super(RaidBosses.getPluginInstance());
    }

    @Override
    public String getDisplayName() {
        return "Water Mage Trainer";
    }

    @Override
    protected List<MerchantRecipe> getRecipes() {
        List<MerchantRecipe> result = new LinkedList<>();

        // Healing Beam Spells
        result.add(this.buildRecipe(plugin.getSkillFactory().buildSkillItem("HEAL_BEAM_LVL_1"),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_1", 5), null).get());

        result.add(this.buildRecipe(plugin.getSkillFactory().buildSkillItem("HEAL_BEAM_LVL_2"),
                plugin.getSkillFactory().buildSkillItem("HEAL_BEAM_LVL_1"),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_1", 10)).get());

        // Group Healing
        result.add(this.buildRecipe(plugin.getSkillFactory().buildSkillItem("GROUP_HEALING_LVL_1"),
                plugin.getItemsFactory().buildItemItem("TOKEN_LVL_1", 10), null).get());

        return result;
    }

    @Override
    public String getInternalName() {
        return "WATER_MAGE_TRADER_1";
    }

}
