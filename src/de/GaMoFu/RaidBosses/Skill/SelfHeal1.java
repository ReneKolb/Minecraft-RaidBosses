package de.GaMoFu.RaidBosses.Skill;

public class SelfHeal1 extends SelfHeal {

    public static final String INTERNAL_NAME = "SELF_HEAL_LVL_1";

    @Override
    public String getSkillInternalName() {
        return INTERNAL_NAME;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    protected double getHealAmount() {
        return 5;
    }

}
