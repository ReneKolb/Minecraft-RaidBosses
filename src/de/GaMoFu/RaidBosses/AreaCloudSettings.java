package de.GaMoFu.RaidBosses;

import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;

import de.GaMoFu.RaidBosses.Skill.ISkill;

public class AreaCloudSettings {

    private double damage;
    private int damageDelayTicks;
    private int damageDelayTmr;

    private int saturation;
    private int saturationDelayTicks;
    private int saturationDelayTmr;

    private ISkill onEndSkill;
    private SpawnedMonster endCaster;
    private Location endCastAt;

    private ISkill onTickSkill;
    private SpawnedMonster tickCaster;
    private Location tickCastAt;

    private int tickTimer;

    public AreaEffectCloud cloud;

    public AreaCloudSettings(double damage, int damageDelayTicks, int saturation, int saturationDelayTicks) {
        this.damage = damage;
        this.damageDelayTicks = damageDelayTicks;
        this.damageDelayTmr = 0;

        this.saturation = saturation;
        this.saturationDelayTicks = saturationDelayTicks;
        this.saturationDelayTmr = 0;

        this.tickTimer = 0;
    }

    public void setOnEndSkill(ISkill skill, SpawnedMonster caster) {
        this.setOnEndSkill(skill, caster, null);
    }

    public void setOnEndSkill(ISkill skill, SpawnedMonster caster, Location castAt) {
        this.onEndSkill = skill;
        this.endCaster = caster;
        this.endCastAt = castAt;
    }

    public void setOnTickSkill(ISkill skill, SpawnedMonster caster) {
        this.setOnTickSkill(skill, caster, null);
    }

    public void setOnTickSkill(ISkill skill, SpawnedMonster caster, Location castAt) {
        this.onTickSkill = skill;
        this.tickCaster = caster;
        this.tickCastAt = castAt;

    }

    /**
     * @return the damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @return the saturation
     */
    public int getSaturation() {
        return saturation;
    }

    public void updateTimers(int amount) {
        if (damage != 0) {
            this.damageDelayTmr += amount;
        }

        if (saturation != 0) {
            this.saturationDelayTmr += amount;
        }
    }

    public void updateTickTimer(int amount) {
        this.tickTimer += amount;
    }

    public void resetTickTimer() {
        this.tickTimer = 0;
    }

    public int getTickTimer() {
        return this.tickTimer;
    }

    public boolean isDamageReady() {
        return damage != 0 && this.damageDelayTmr >= this.damageDelayTicks;
    }

    public boolean isSaturationReady() {
        return saturation != 0 && this.saturationDelayTmr >= this.saturationDelayTicks;
    }

    public void resetDamageTmr() {
        this.damageDelayTmr = 0;
    }

    public void resetSaturationTmr() {
        this.saturationDelayTmr = 0;
    }

    public void onEnd() {
        if (this.onEndSkill != null) {
            if (this.endCastAt != null) {
                onEndSkill.execute(endCaster, endCastAt);
            } else {
                onEndSkill.execute(endCaster);
            }
        }
    }

    public void onTick() {
        if (this.onTickSkill != null) {
            if (this.tickCastAt != null) {
                onTickSkill.execute(tickCaster, tickCastAt);
            } else {
                onTickSkill.execute(tickCaster);
            }
        }
    }

}
