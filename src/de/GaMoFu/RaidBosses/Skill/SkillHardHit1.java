package de.GaMoFu.RaidBosses.Skill;

public class SkillHardHit1 extends SkillHardHit {

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public float getKnockbackMulti() {
        return 2;
    }

    @Override
    public float getAttackRange() {
        return 2.5f;
    }

    @Override
    public double getDamage() {
        return 5;
    }

}
