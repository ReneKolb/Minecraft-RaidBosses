package de.GaMoFu.RaidBosses.Monsters;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.Skill.ISkill;

public class Zombie extends Monster<org.bukkit.entity.Zombie> {

	public Zombie() {
		super(org.bukkit.entity.Zombie.class);
	}

	public static final String ALIAS = "zombie";

	@Override
	public void loop() {
		super.loop();
	}

	@Override
	protected void afterSpawn() {
		this.entity.setBaby(false);

		this.entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0);
		// (Zombie) this.entity

	}

	@Override
	public double getMaxHealth() {
		return 20;
	}

	@Override
	public String getDisplayName() {
		return "Zompie";
	}

	@Override
	public double getPullAggroRangeSquared() {
		return 13 * 13;
	}

	@Override
	public List<ISkill> createSkillList() {
		return new LinkedList<>();
	}
}
