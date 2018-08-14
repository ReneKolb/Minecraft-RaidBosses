package de.GaMoFu.RaidBosses.Skill;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.Dungeon;
import de.GaMoFu.RaidBosses.SpawnedMonster;

public abstract class HealBeam implements ISkill {

	private class LivingEntityMap {
		private UUID uuid;
		private LivingEntity le;

		public LivingEntityMap(LivingEntity le) {
			this.uuid = le.getUniqueId();
			this.le = le;
		}

		public LivingEntity getLivingEntity() {
			return this.le;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (o == this)
				return true;
			if (!(o instanceof LivingEntityMap))
				return false;
			LivingEntityMap lem = (LivingEntityMap) o;
			return lem.uuid.equals(this.uuid);
		}
	}

	protected static final float HEAL_JUMP_TARGET_DISTANCE = 5f;
	protected static final float HEAL_FIRST_RANGE = 8f;

	public abstract int getNumTargets();

	public abstract int getLevel();

	public abstract double getHealAmount();

	@Override
	public String getSkillDisplayName() {
		return "Healing Beam Lv." + getLevel();
	}

	@Override
	public String getSkillInternalName() {
		return "HEAL_BEAM_LVL_" + getLevel();
	}

	@Override
	public boolean execute(Player executer) {
		SortedSet<Distance<LivingEntity>> result = getEntitiesInFront(executer, false, HEAL_FIRST_RANGE);

		if (result.isEmpty())
			return false;

		List<LivingEntityMap> hitTarget = new ArrayList<>();
		hitTarget.add(new LivingEntityMap(executer)); // the source of the healing should be included in the visual line

		LivingEntity target = result.first().getObject();
		LivingEntityMap targetM = new LivingEntityMap(target);
		hitTarget.add(targetM);

		double h = target.getHealth();
		double mh = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

		target.setHealth(Math.min(mh, h + getHealAmount()));
		Bukkit.getServer().getPluginManager()
				.callEvent(new EntityRegainHealthEvent(target, getHealAmount(), RegainReason.MAGIC));

		World world = executer.getWorld();

		LivingEntityMap lastTarget = targetM;
		boolean added = false;
		for (int i = 1; i < getNumTargets(); i++) {
			added = false;
			for (Player p : world.getPlayers()) {

				LivingEntityMap newEntry = new LivingEntityMap(p);
				if (p.getHealth() < p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()
						&& p.getLocation()
								.distance(lastTarget.getLivingEntity().getLocation()) <= HEAL_JUMP_TARGET_DISTANCE
						&& !hitTarget.contains(newEntry)) {
					hitTarget.add(newEntry);

					h = p.getHealth();
					mh = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

					p.setHealth(Math.min(mh, h + getHealAmount()));
					Bukkit.getServer().getPluginManager()
							.callEvent(new EntityRegainHealthEvent(p, getHealAmount(), RegainReason.MAGIC));

					lastTarget = newEntry;
					added = true;
					break;
				}
			}
			if (!added)
				break;

		}

		// Display healing Line

		Vector loc1 = hitTarget.get(0).getLivingEntity().getLocation().toVector();
		Vector y = new Vector(0, 1, 0);
		Vector loc2;
		for (int i = 1; i < hitTarget.size(); i++) {
			loc2 = hitTarget.get(i).getLivingEntity().getLocation().toVector();

			// Line from loc1 to loc2
			Dungeon.displayLine(world, loc1.clone().add(y), loc2.clone().add(y));

			loc1 = loc2.clone();
		}

		world.playSound(executer.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f);

		return true;
	}

	@Override
	public boolean execute(SpawnedMonster executer) {
		System.out.println(
				"Healing Beam is not supported for monsters. (" + executer.getMonsterEntity().getDisplayName() + ")");
		return false;
	}

	@Override
	public boolean execute(SpawnedMonster executer, Location targetLoc) {
		System.out.println("Healing Beam is not supported for monsters");
		return false;
	}

	@Override
	public int getBasicHungerCost() {
		return 6;
	}

	@Override
	public int getCooldownTicks() {
		return 4;
	}

	@Override
	public Material getDisplayMaterial() {
		return Material.CACTUS_GREEN;
	}

	@Override
	public short getDisplayDurability() {
		return 0;
	}

	@Override
	public List<String> getLore() {
		List<String> result = new LinkedList<>();
		if (getNumTargets() > 1) {
			result.add("The healing beam hits up to " + getNumTargets() + " targets");
		}
		return null;
	}
	
	@Override
	public boolean isTriggerGlobalCooldown() {
		return true;
	}

}
