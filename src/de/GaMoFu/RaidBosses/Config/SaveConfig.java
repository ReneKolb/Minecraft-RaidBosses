package de.GaMoFu.RaidBosses.Config;

import java.util.ArrayList;
import java.util.Collection;

public class SaveConfig {

	private Collection<MonsterConfig> monsterConfig;

	private Collection<MonsterConfig> bossesConfig;

	public SaveConfig(Collection<MonsterConfig> monsterConfig, Collection<MonsterConfig> bossesConfig) {
		this.monsterConfig = monsterConfig;
		this.bossesConfig = bossesConfig;

	}

	/**
	 * @return the monsterConfig
	 */
	public Collection<MonsterConfig> getMonsterConfig() {
		if (monsterConfig == null) {
			monsterConfig = new ArrayList<>();
		}
		return monsterConfig;
	}

	/**
	 * @return the bossesConfig
	 */
	public Collection<MonsterConfig> getBossesConfig() {
		if (bossesConfig == null) {
			bossesConfig = new ArrayList<>();
		}
		return bossesConfig;
	}

}
