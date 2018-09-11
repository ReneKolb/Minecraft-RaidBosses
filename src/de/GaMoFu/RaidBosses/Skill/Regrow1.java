package de.GaMoFu.RaidBosses.Skill;

public class Regrow1 extends Regrow{

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getEffectDurationTicks() {
        return 20*60;
    }

    @Override
    public float getRadius() {
        return 4;
    }

}
