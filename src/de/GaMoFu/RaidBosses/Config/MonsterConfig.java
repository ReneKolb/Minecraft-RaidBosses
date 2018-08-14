package de.GaMoFu.RaidBosses.Config;

public class MonsterConfig {

	private long configID;

	private String monsterTypeAlias;

	private double x;

	private double y;

	private double z;

	private IdleWalkSettings idleWalkSettings;

	private Integer lootChestX;

	private Integer lootChestY;

	private Integer lootChestZ;

	/**
	 * @return the configID
	 */
	public long getConfigID() {
		return configID;
	}

	/**
	 * @param configID
	 *            the configID to set
	 */
	public void setConfigID(long configID) {
		this.configID = configID;
	}

	/**
	 * @return the monsterType
	 */
	public String getMonsterTypeAlias() {
		return monsterTypeAlias;
	}

	/**
	 * @param monsterType
	 *            the monsterType to set
	 */
	public void setMonsterType(String monsterTypeAlias) {
		this.monsterTypeAlias = monsterTypeAlias;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	// public String toSaveString() {
	// return this.configID+";"+this.monsterTypeAlias + ";" + this.x + ";" + this.y
	// + ";" + this.z;
	// }
	//
	// public static MonsterConfig fromSaveString(String saveString) {
	// String[] split = saveString.split(";");
	//
	// if (split.length != 5) {
	// throw new RuntimeException("Cannot create MonsterConfig from save String '" +
	// saveString + "'");
	// }
	//
	// MonsterConfig result = new MonsterConfig();
	// result.configID = Long.parseLong(split[0]);
	// result.monsterTypeAlias = split[1];
	// result.x = Double.parseDouble(split[2]);
	// result.y = Double.parseDouble(split[3]);
	// result.z = Double.parseDouble(split[4]);
	// return result;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MonsterConfig [monsterTypeAlias=" + monsterTypeAlias + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	/**
	 * @return the idleWalkSettings
	 */
	public IdleWalkSettings getIdleWalkSettings() {
		return idleWalkSettings;
	}

	/**
	 * @param idleWalkSettings
	 *            the idleWalkSettings to set
	 */
	public void setIdleWalkSettings(IdleWalkSettings idleWalkSettings) {
		this.idleWalkSettings = idleWalkSettings;
	}

	/**
	 * @return the lootChestX
	 */
	public Integer getLootChestX() {
		return lootChestX;
	}

	/**
	 * @param lootChestX
	 *            the lootChestX to set
	 */
	public void setLootChestX(Integer lootChestX) {
		this.lootChestX = lootChestX;
	}

	/**
	 * @return the lootChestY
	 */
	public Integer getLootChestY() {
		return lootChestY;
	}

	/**
	 * @param lootChestY
	 *            the lootChestY to set
	 */
	public void setLootChestY(Integer lootChestY) {
		this.lootChestY = lootChestY;
	}

	/**
	 * @return the lootChestZ
	 */
	public Integer getLootChestZ() {
		return lootChestZ;
	}

	/**
	 * @param lootChestZ
	 *            the lootChestZ to set
	 */
	public void setLootChestZ(Integer lootChestZ) {
		this.lootChestZ = lootChestZ;
	}

}
