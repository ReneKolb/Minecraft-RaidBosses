package de.GaMoFu.RaidBosses.Skill;

public class Fireball1 extends Fireball {

    public static final String INTERNAL_NAME = "FIREBALL_LVL_1";

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public double getDamage() {
        return 6;
    }

    @Override
    public String getSkillInternalName() {
        return INTERNAL_NAME;
    }

}
