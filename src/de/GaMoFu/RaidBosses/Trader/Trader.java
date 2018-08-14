package de.GaMoFu.RaidBosses.Trader;

import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import de.GaMoFu.RaidBosses.RaidBosses;

public abstract class Trader {

	protected RaidBosses plugin;

	protected Villager villager;

	public abstract String getDisplayName();
	
	public abstract String getInternalName();

	public Trader(RaidBosses plugin) {
		this.plugin = plugin;

	}

	protected abstract List<MerchantRecipe> getRecipes();

	protected Optional<MerchantRecipe> buildRecipe(Optional<ItemStack> result, Optional<ItemStack> ingredient1,
			Optional<ItemStack> ingredient2) {

		if (result == null || !result.isPresent())
			return Optional.empty();

		MerchantRecipe recipe = new MerchantRecipe(result.get(), Integer.MAX_VALUE);

		if (ingredient1 != null && ingredient1.isPresent())
			recipe.addIngredient(ingredient1.get());

		if (ingredient2 != null && ingredient2.isPresent())
			recipe.addIngredient(ingredient2.get());

		if (recipe.getIngredients().isEmpty())
			return Optional.empty();

		recipe.setExperienceReward(false);

		return Optional.of(recipe);
	}

	public void spawn(Location spawnLocation) {
		this.villager = (Villager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
		this.villager.setAI(false);
		this.villager.setRemoveWhenFarAway(false);
		this.villager.setCustomName(getDisplayName());
		this.villager.setCustomNameVisible(true);

		this.villager.setRecipes(getRecipes());

	}

}
