package de.GaMoFu.RaidBosses.Skill;

public class HealBeam2 extends HealBeam {

    @Override
    public int getNumTargets() {
        return 2;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public double getHealAmount() {
        return 8;
    }

}
