package de.GaMoFu.RaidBosses.Monsters;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.Skill.ISkill;

public class Spider extends Monster<org.bukkit.entity.Spider> {
	
	public static final String ALIAS = "spider";

	public Spider() {
		super(org.bukkit.entity.Spider.class);
	}

	@Override
	public List<ISkill> createSkillList() {
		return null;
	}

	@Override
	public double getMaxHealth() {
		return 16;
	}

	@Override
	public String getDisplayName() {
		return "Spider";
	}

	@Override
	public double getPullAggroRangeSquared() {
		return 13 * 13;
	}

	@Override
	protected void afterSpawn() {

	}

}
