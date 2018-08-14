package de.GaMoFu.RaidBosses.Monsters;

import java.util.List;

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

}
