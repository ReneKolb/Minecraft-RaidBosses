package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import de.GaMoFu.RaidBosses.AreaCloudSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;

public abstract class Refreshment implements ISkill {

	public abstract int getLevel();

	public abstract float getRange();

	public abstract int getDurationTicks();

	@Override
	public String getSkillDisplayName() {
		return "Refreshment Lv." + getLevel();
	}

	@Override
	public String getSkillInternalName() {
		return "REFRESHMENT_LVL_" + getLevel();
	}

	@Override
	public boolean execute(Player executer) {
		World w = executer.getWorld();

		// TODO: saturation is not increasing -.-
		AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(executer.getLocation(), EntityType.AREA_EFFECT_CLOUD);
		cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

		cloud.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 0), true); // override

		cloud.setColor(Color.fromRGB(99, 52, 14)); // =00ffb7
		cloud.setDuration(getDurationTicks());
		cloud.setRadius(getRange());
		cloud.setRadiusOnUse(0); // dont decrease the radius on use
		cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
		cloud.setReapplicationDelay(15);
		cloud.setSource(executer);
		cloud.setWaitTime(0); // instantly apply effect

		AreaCloudSettings settings = new AreaCloudSettings(0, 0, 1, 20);
		RaidBosses.getPluginInstance().getAreaCloudHandler().handleCustomAreaEffectCloud(cloud, settings);

		return true;
	}

	@Override
	public boolean execute(SpawnedMonster executer) {
		System.out.println("Refreshment is currentyl not supported for monsters");
		return false;
	}

	@Override
	public boolean execute(SpawnedMonster executer, Location targetLoc) {
		System.out.println("Refreshment is currentyl not supported for monsters");
		return false;
	}

	@Override
	public int getBasicHungerCost() {
		return 0;
	}

	@Override
	public int getCooldownTicks() {
		return 20 * 60;
	}

	@Override
	public Material getDisplayMaterial() {
		return Material.MELON;
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList("Refreshment");
	}
	
	@Override
	public boolean isTriggerGlobalCooldown() {
		return true;
	}

}
