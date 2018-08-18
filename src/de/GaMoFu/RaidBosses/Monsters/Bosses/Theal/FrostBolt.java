package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class FrostBolt implements ISkill {

    @Override
    public int getLevel() {
        return -1;
    }
    
    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Frost Bolt";
    }

    @Override
    public String getSkillInternalName() {
        return "FROST_BOLT";
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
        dir.multiply(0.8);

        ParticleEffect eff = new ParticleEffect(Particle.SNOWBALL, 20, 0, 0, 0, 0);
        RaidBosses.getPluginInstance().getProjectileManager().launchProjectile(Arrays.asList(eff), Arrays.asList(), 1,
                20, 10, false, c, dir, c.getEyeLocation());

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("Frost bolt does not support TargetLoc");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 3 * 20;
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
