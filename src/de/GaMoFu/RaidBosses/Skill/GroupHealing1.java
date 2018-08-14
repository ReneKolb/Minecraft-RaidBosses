package de.GaMoFu.RaidBosses.Skill;

public class GroupHealing1 extends GroupHealing {

    @Override
    protected int getLevel() {
        return 1;
    }

    @Override
    public double getHealingAmount() {
        return 6;
    }

    @Override
    public float getHealingRange() {
        return 6;
    }

}
