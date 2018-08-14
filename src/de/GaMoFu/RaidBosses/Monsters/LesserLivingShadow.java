package de.GaMoFu.RaidBosses.Monsters;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.PigZombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.ParticleEffects.SphereEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class LesserLivingShadow extends Monster<PigZombie> {

	public static final String ALIAS = "LesserLivingShadow";

	private int effectTmr;

	public LesserLivingShadow() {
		super(PigZombie.class);
		this.effectTmr = 0;
	}

	@Override
	public List<ISkill> createSkillList() {
		return null;
	}

	@Override
	public double getMaxHealth() {
		return 35;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.DARK_PURPLE + "Lesser Living Shadow";
	}

	@Override
	public double getPullAggroRangeSquared() {
		return 100;
	}

	@Override
	protected void afterSpawn() {
		this.entity.setBaby(false);

		this.entity.getEquipment().clear();

		this.entity.setSilent(true);

		this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0), true);

		this.entity.setCanPickupItems(false);
		this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);

	}

	@Override
	public void loop() {
		super.loop();

		this.effectTmr++;
		if (this.effectTmr >= 5) {
			this.effectTmr = 0;

			Location loc = this.getEntity().getLocation();
			SphereEffect.doEffect(loc.add(0, 0.5, 0), Particle.SPELL_WITCH, 1, 0, 0, 0, 40, 1);
		}
	}

}
