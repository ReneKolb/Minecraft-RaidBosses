package de.GaMoFu.RaidBosses.Config;

import de.GaMoFu.RaidBosses.IdleWalk;

public abstract class IdleWalkSettings {

	public abstract Class<? extends IdleWalk<? extends IdleWalkSettings>> getIdleWalkClass();

}
