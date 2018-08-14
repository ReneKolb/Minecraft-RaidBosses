package de.GaMoFu.RaidBosses;

import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.entity.Creature;

import de.GaMoFu.RaidBosses.Config.IdleWalkSettings;
import de.GaMoFu.RaidBosses.Monsters.Monster;

public abstract class IdleWalk<T extends IdleWalkSettings> {

	protected T idleWalkSettings;

	protected Monster<? extends Creature> monster;

	private CraftCreature craftCreature;

	public boolean isWalking() {
		if (this.monster.getEntity() == null)
			return false;

		// m() is the current PathEntity. If it's null, the entity is not walking on a
		// path
		return getCraftCreature().getHandle().getNavigation().m() != null;
	}

	public void setMonster(Monster<? extends Creature> monster) {
		this.monster = monster;
	}

	public void setIdleWalkSettings(T idleWalkSettings) {
		this.idleWalkSettings = idleWalkSettings;
	}

	public CraftCreature getCraftCreature() {
		// lazy initializing monster since the actual entity is only spawned in loop()
		// not at construction time
		if (this.craftCreature == null) {
			this.craftCreature = (CraftCreature) this.monster.getEntity();
		}
		return this.craftCreature;
	}

	public abstract void loop();

}
