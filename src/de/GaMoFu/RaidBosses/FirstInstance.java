package de.GaMoFu.RaidBosses;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;

import de.GaMoFu.RaidBosses.Dungeons.Dungeon;
import de.GaMoFu.RaidBosses.Events.FightEndEvent;
import de.GaMoFu.RaidBosses.Events.FightStartEvent;
import de.GaMoFu.RaidBosses.Monsters.Zomboss;

public class FirstInstance extends Dungeon {

    public static final String ALIAS = "first";
    
    @Override
    public void init(RaidBosses plugin, World world) {
        super.init(plugin, world);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return super.getPlayerSpawnLocation();
    }

    @Override
    public void loop() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAlias() {
        return ALIAS;
    }

    @Override
    public String getDisplayName() {
        return "First Dungeon";
    }

    private void startZomboss() {
        // Close entrance

        // 2,13,-17
        // -2,11,-17

        for (int x = -2; x <= 2; x++) {
            for (int y = 11; y <= 13; y++) {
                Block block = this.world.getBlockAt(x, y, -17);
                block.setType(Material.IRON_BARS);
            }
        }

    }

    private void endZomboss() {
        // Open entrance

        for (int x = -2; x <= 2; x++) {
            for (int y = 11; y <= 13; y++) {
                Block block = this.world.getBlockAt(x, y, -17);
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onBossFightStartEvent(FightStartEvent onFightStart) {
        Optional<SpawnedBoss> oBoss = this.getSpawnedBoss(onFightStart.getMonster().getEntity().getUniqueId());

        if (!oBoss.isPresent())
            return;

        SpawnedBoss boss = oBoss.get();

        if (boss.getMonsterEntity() instanceof Zomboss) {
            startZomboss();
        }
    }

    @EventHandler
    public void onBossFightEndEvent(FightEndEvent onFightEnd) {
        Optional<SpawnedBoss> oBoss = this.getSpawnedBoss(onFightEnd.getMonster().getEntity().getUniqueId());

        if (!oBoss.isPresent())
            return;

        SpawnedBoss boss = oBoss.get();

        if (boss.getMonsterEntity() instanceof Zomboss) {
            endZomboss();
        }
    }

}
