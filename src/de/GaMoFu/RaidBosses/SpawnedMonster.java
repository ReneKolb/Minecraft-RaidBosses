package de.GaMoFu.RaidBosses;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.Dungeons.Dungeon;
import de.GaMoFu.RaidBosses.Monsters.Monster;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class SpawnedMonster {

    protected Location spawnLocation;

    protected long configID;

    protected Monster<? extends Creature> monsterEntity;

    protected Dungeon dungeon;

    protected Set<String> skillCooldowns;

    protected boolean globalCooldown;

    protected int skillLoopTmr;

    public SpawnedMonster(Dungeon dungeon, long associatedConfigID) {
        this.dungeon = dungeon;
        this.configID = associatedConfigID;

        skillLoopTmr = 5;

        this.skillCooldowns = new HashSet<>();
    }

    public void loop() {
        if (this.monsterEntity == null || this.monsterEntity.getEntity() == null
                || this.monsterEntity.getEntity().isDead())
            return;

        this.monsterEntity.loop();
        handleSkills();
    }

    public void dropLoot() {
        LootTable lootTable = monsterEntity.getLootTable();
        if (lootTable == null)
            return;

        Collection<ItemStack> loot = lootTable.populateLoot(RaidBosses.random, null);

        Location loc = monsterEntity.getEntity().getLocation();

        for (ItemStack itemStack : loot) {
            loc.getWorld().dropItemNaturally(loc, itemStack);
        }
    }

    /**
     * @return the spawnLocation
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * @return the dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * @param spawnLocation
     *            the spawnLocation to set
     */
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * @return the configID
     */
    public long getConfigID() {
        return configID;
    }

    /**
     * @return the monsterEntity
     */
    public Monster<? extends Creature> getMonsterEntity() {
        return monsterEntity;
    }

    /**
     * @param monsterEntity
     *            the monsterEntity to set
     */
    public void setMonsterEntity(Monster<? extends Creature> monsterEntity) {
        this.monsterEntity = monsterEntity;
    }

    private void handleSkills() {

        if (!this.getMonsterEntity().isInFight())
            return;

        if (this.skillLoopTmr > 0) {
            this.skillLoopTmr--;
            if (this.skillLoopTmr <= 0) {
                this.skillLoopTmr = 5;

                if (this.globalCooldown) {
                    // do not use skills when global cooldown
                    return;
                }

                for (ISkill skillToUse : this.monsterEntity.getSkills()) {
                    // Check if the skill is on cooldown
                    if (this.skillCooldowns.contains(skillToUse.getSkillInternalName())) {
                        continue;
                    }

                    final ISkill skill = skillToUse;
                    boolean usedSuccessful = skill.execute(this);

                    if (!usedSuccessful)
                        continue;

                    // add the skill to the cooldown list
                    this.skillCooldowns.add(skill.getSkillInternalName());

                    if (skill.isTriggerGlobalCooldown()) {
                        this.globalCooldown = true;

                        Bukkit.getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                            @Override
                            public void run() {
                                globalCooldown = false;
                            }

                        }, monsterEntity.getGlobalCooldownTicks());
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                        @Override
                        public void run() {
                            skillCooldowns.remove(skill.getSkillInternalName());
                        }

                    }, skill.getCooldownTicks());

                    // Skip further skills, since global cooldown is triggered. otherwise multiple
                    // skills may be used at the same time
                    if (this.globalCooldown)
                        return;
                }
            }
        }
    }

}
