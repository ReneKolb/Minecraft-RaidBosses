package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class Fireball implements ISkill {

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Fireball";
    }

    public abstract double getDamage();

    @Override
    public boolean execute(Player executer) {

        Vector dir = executer.getLocation().getDirection().clone();
        dir.normalize();
        // dir.multiply(0.8);

        ParticleEffect fly1 = new ParticleEffect(Particle.SMOKE_NORMAL, 10, 0.1, 0.1, 0.1, 0);
        ParticleEffect fly2 = new ParticleEffect(Particle.FLAME, 15, 0.1, 0.1, 0.1, 0);

        // ParticleEffect hit1 = new ParticleEffect(Particle.EXPLOSION_HUGE, 1, 1, 1, 1,
        // 1);
        // ParticleEffect hit2 = new ParticleEffect(Particle.FLAME, 25, 1, 1, 1, 1);
        // ParticleEffect hit3 = new ParticleEffect(Particle.SMOKE_NORMAL, 25, 1, 1, 1,
        // 1);
        // ParticleEffect hit4 = new ParticleEffect(Particle.LAVA, 25, 1, 1, 1, 1);
        // ParticleEffect hit5 = new ParticleEffect(Particle.LAVA, 25, 1, 1, 1, 1);
        // ParticleEffect hit6 = new ParticleEffect(Particle.REDSTONE, 50, 1, 1, 1,
        // 0.01);

        Location sourceLoc = executer.getEyeLocation();
        sourceLoc.setDirection(dir);

        RaidBosses.getPluginInstance().getProjectileManager().launchProjectile(Arrays.asList(fly1, fly2), null, 1, 50,
                getDamage(), false, executer, dir, sourceLoc);

        executer.getWorld().playSound(executer.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2, 0.5f);

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 2;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 2;
    }

    @Override
    public Material getDisplayMaterial() {
        return null;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
