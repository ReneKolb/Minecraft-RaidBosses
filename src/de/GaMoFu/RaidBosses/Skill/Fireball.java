package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

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
        ParticleEffect hitEffect = new ParticleEffect(Particle.LAVA, 25, 0.2, 0.2, 0.2, 0.3);

        SmallFireball sfb = executer.launchProjectile(SmallFireball.class);
        RaidBosses.getPluginInstance().getProjectileManager().addCustomDamageProjectile(sfb, getDamage(),
                Arrays.asList(hitEffect));

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
        return 1;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 2;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.FIRE_CHARGE;
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
