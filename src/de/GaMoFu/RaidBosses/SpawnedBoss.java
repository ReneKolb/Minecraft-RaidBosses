package de.GaMoFu.RaidBosses;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

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

    @Override
    protected void dropLoot() {
        if (this.getLootChestLocation() == null) {
            super.dropLoot();
            return;
        }

        LootTable lootTable = this.getMonsterEntity().getLootTable();
        if (lootTable == null) {
            return;
        }

        BlockState bs = lootChestLocation.getBlock().getState();
        if (!(bs instanceof Chest)) {
            super.dropLoot();
            return;
        }

        World world = getLootChestLocation().getWorld();

        world.spawnParticle(Particle.FLAME, lootChestLocation.clone().add(0.5, 0.5, 0.5), 30, 0.2d, 0.2d, 0.2d);

        Chest chest = (Chest) bs;
        Inventory inv = chest.getBlockInventory();

        lootTable.fillInventory(inv, RaidBosses.random, null);
    }
}
