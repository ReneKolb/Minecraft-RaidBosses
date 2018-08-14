package de.GaMoFu.RaidBosses;

import org.bukkit.Location;

public class SpawnedBoss extends SpawnedMonster {

    protected Location lootChestLocation;

    public SpawnedBoss(Dungeon dungeon, long associatedConfigID) {
        super(dungeon, associatedConfigID);
    }

    /**
     * @return the lootChestLocation
     */
    public Location getLootChestLocation() {
        return lootChestLocation;
    }

    /**
     * @param lootChestLocation
     *            the lootChestLocation to set
     */
    public void setLootChestLocation(Location lootChestLocation) {
        this.lootChestLocation = lootChestLocation;
    }

}
