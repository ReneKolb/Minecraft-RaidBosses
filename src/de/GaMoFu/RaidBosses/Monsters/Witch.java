package de.GaMoFu.RaidBosses.Monsters;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.Skill.ISkill;

public class Witch extends Monster<org.bukkit.entity.Witch> {

    public static final String ALIAS = "witch";

    public Witch() {
        super(org.bukkit.entity.Witch.class);
    }

    @Override
    public List<ISkill> createSkillList() {
        return null;
    }

    @Override
    public double getMaxHealth() {
        return 26;
    }

    @Override
    public String getDisplayName() {
        return "Dirty Bitch ehh Witch";
    }

    @Override
    public double getPullAggroRangeSquared() {
        return 12 * 12;
    }

    @Override
    protected void afterSpawn() {
    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_WITCH_AMBIENT, 1f, 1f);
    }

    @Override
    public LootTable getLootTable() {
        return null;
    }

}
