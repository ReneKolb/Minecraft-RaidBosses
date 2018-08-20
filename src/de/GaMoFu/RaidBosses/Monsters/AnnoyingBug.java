package de.GaMoFu.RaidBosses.Monsters;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Silverfish;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.Skill.ISkill;

public class AnnoyingBug extends Monster<Silverfish> {

    public static final String ALIAS = "annoyingbug";

    public AnnoyingBug() {
        super(Silverfish.class);
    }

    @Override
    public List<ISkill> createSkillList() {
        return new LinkedList<>();
    }

    @Override
    public double getMaxHealth() {
        return 3;
    }

    @Override
    public String getDisplayName() {
        return "Annoying Bug";
    }

    @Override
    public double getPullAggroRangeSquared() {
        return 9 * 9;
    }

    @Override
    protected void afterSpawn() {
        // Hide in Block extends RandomStroll, so no need to extra remove it
    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_SILVERFISH_HURT, 1f, 1f);
    }

    @Override
    public LootTable getLootTable() {
        return null;
    }

}
