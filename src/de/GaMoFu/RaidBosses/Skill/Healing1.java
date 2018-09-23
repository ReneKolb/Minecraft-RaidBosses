package de.GaMoFu.RaidBosses.Skill;

public class Healing1 extends Healing {

    public static final String INTERNAL_NAME = "HEALING_LVL_1";

    @Override
    public String getSkillInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public double getHealAmount() {
        return 7;
    }

}
