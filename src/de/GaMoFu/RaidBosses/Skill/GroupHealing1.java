package de.GaMoFu.RaidBosses.Skill;

@Deprecated //Use Regrow 
public class GroupHealing1 extends GroupHealing {

    @Override
    public int getLevel() {
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
