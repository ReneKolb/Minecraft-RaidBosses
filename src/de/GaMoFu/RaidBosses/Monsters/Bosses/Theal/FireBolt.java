package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class FireBolt implements ISkill {

    @Override
    public int getLevel() {
        return -1;
    }
    
    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Fire Bolt";
    }

    @Override
    public String getSkillInternalName() {
        return "FIRE_BOLT";
    }

    @Override
    public boolean execute(Player executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        Creature c = executer.getMonsterEntity().getEntity();

        // Get random target
        UUID targetID = executer.getMonsterEntity().getRandomAggroTarget();

        LivingEntity target = null;
        for (LivingEntity le : c.getWorld().getLivingEntities()) {
            if (le.getUniqueId().equals(targetID)) {
                target = le;
                break;
            }
        }

        if (target == null) {
            // Handle if no target?
            System.out.println("No target. abort FrostBolt");
            return false;
        }

        Vector dir = target.getEyeLocation().subtract(c.getEyeLocation()).toVector();
        dir.normalize();
        // dir.multiply(0.8);

        ParticleEffect fly1 = new ParticleEffect(Particle.SMOKE_NORMAL, 10, 0.1, 0.1, 0.1, 0);
        ParticleEffect fly2 = new ParticleEffect(Particle.FLAME, 15, 0.1, 0.1, 0.1, 0);

        ParticleEffect hit1 = new ParticleEffect(Particle.EXPLOSION_HUGE, 1, 1, 1, 1, 1);
        ParticleEffect hit2 = new ParticleEffect(Particle.FLAME, 25, 1, 1, 1, 1);
        ParticleEffect hit3 = new ParticleEffect(Particle.SMOKE_NORMAL, 25, 1, 1, 1, 1);
        ParticleEffect hit4 = new ParticleEffect(Particle.LAVA, 25, 1, 1, 1, 1);
        ParticleEffect hit5 = new ParticleEffect(Particle.LAVA, 25, 1, 1, 1, 1);
        ParticleEffect hit6 = new ParticleEffect(Particle.REDSTONE, 50, 1, 1, 1, 0.01);

        Location sourceLoc = c.getEyeLocation();
        sourceLoc.setDirection(dir);

        RaidBosses.getPluginInstance().getProjectileManager().launchAimingProjectile(Arrays.asList(fly1, fly2),
                Arrays.asList(hit1, hit2, hit3, hit4, hit5, hit6), 1, 50, 15, false, c, sourceLoc, 0.3, 2.0, target);

        c.getWorld().playSound(c.getLocation(), Sound.ENTITY_GHAST_SHOOT, 2, 0.5f);

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("Fire bolt does not support TargetLoc");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 100;
    }

    @Override
    public Material getDisplayMaterial() {
        return null;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        return null;
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
