package de.GaMoFu.RaidBosses.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;

public class SkillFactory implements Listener {

	private RaidBosses plugin;

	/** For looking up a skill by its internal name */
	private Map<String, ISkill> skillNameLookup; // SkillName -> Skill

	/** For looking up a skill be an held item's display name */
	private Map<String, ISkill> skillDisplayNameLookup; // DisplayString -> Skill

	public SkillFactory(RaidBosses plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.skillNameLookup = new HashMap<>();
		this.skillDisplayNameLookup = new HashMap<>();

		init();
	}

	private void init() {
		this.plugin.getLogger().info("Initializing skills");

		for (SkillsEnum s : SkillsEnum.values()) {
			Class<? extends ISkill> skillClass = s.getSkillClass();
			ISkill skill;
			try {
				skill = skillClass.newInstance();

				this.skillNameLookup.put(skill.getSkillInternalName(), skill);
				this.skillDisplayNameLookup.put(skill.getSkillDisplayName(), skill);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isSkillItem(ItemStack item) {
		if (item == null)
			return false;

		ItemMeta meta = item.getItemMeta();
		String displayName = meta.getDisplayName();
		return skillDisplayNameLookup.containsKey(displayName);
	}

	public Optional<ISkill> getSkillFromItemStack(ItemStack item) {
		if (item == null)
			return Optional.empty();

		ItemMeta meta = item.getItemMeta();
		String displayName = meta.getDisplayName();
		if (skillDisplayNameLookup.containsKey(displayName)) {
			return Optional.of(skillDisplayNameLookup.get(displayName));
		} else {
			return Optional.empty();
		}
	}

	public Optional<ItemStack> buildSkillItem(String skillName) {

		ISkill skill = skillNameLookup.get(skillName);
		if (skill == null) {
			return Optional.empty();
		}

		ItemStack result = new ItemStack(skill.getDisplayMaterial(), 1, skill.getDisplayDurability());

		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(skill.getSkillDisplayName());
		meta.setLore(skill.getLore());

		meta.addEnchant(Enchantment.WATER_WORKER, 0, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		result.setItemMeta(meta);
		return Optional.of(result);
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		int newSlot = event.getNewSlot();
		Player player = event.getPlayer();

		if (player.getGameMode() != GameMode.ADVENTURE) {
			return;
		}

		ItemStack newItem = player.getInventory().getItem(newSlot);
		Optional<ISkill> oSkill = getSkillFromItemStack(newItem);

		if (!oSkill.isPresent())
			return;

		event.setCancelled(true);

		ISkill skill = oSkill.get();

		PlayerSettings ps = plugin.getPlayerSettings(player);

		if (!ps.isSkillReady(skill)) {
			player.sendMessage("Skill is not ready");
			return;
		}

		int foodCost = skill.getBasicHungerCost();

		if (player.getFoodLevel() < foodCost) {
			player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
			player.sendMessage("Cannot use skill. Too few food");
			return;
		}

		boolean success = skill.execute(player);
		if (success) {
			player.setFoodLevel(player.getFoodLevel() - foodCost);
			ps.startSkillCooldown(skill);
		}

	}

}
