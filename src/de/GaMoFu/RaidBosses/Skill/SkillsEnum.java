package de.GaMoFu.RaidBosses.Skill;

public enum SkillsEnum {

    //@formatter:off
	HEAL_1(SkillHeal1.class),
	
	HEAL_BEAM_1(HealBeam1.class),
	HEAL_BEAM_2(HealBeam2.class),
	
	REFRESHMENT_1(Refreshment1.class),
	
	REGROW_1(Regrow1.class),
	
//	GROUP_HEALING_1(GroupHealing1.class),
	
	HARD_HIT_1(SkillHardHit1.class),
    
    FIREBALL_1(Fireball1.class);
	//@formatter:on

    // REMEMBER TO CALL event(EntityRegainHealthEvent) WHEN MANUALLY CHANGING
    // PLAYER'S HEALTH

    // Heiler: Esser beizaubern
    // Heal

    private Class<? extends ISkill> skillClass;

    private SkillsEnum(Class<? extends ISkill> skillClass) {
        this.skillClass = skillClass;
    }

    public Class<? extends ISkill> getSkillClass() {
        return skillClass;
    }

}
