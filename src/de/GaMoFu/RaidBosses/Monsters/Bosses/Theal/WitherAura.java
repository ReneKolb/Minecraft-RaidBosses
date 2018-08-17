package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.ParticleEffects.PointEffect;
import de.GaMoFu.RaidBosses.ParticleEffects.SphereEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class WitherAura implements ISkill {

    @Override
    public String getSkillDisplayName() {
        return "Wither Aura";
    }

    @Override
    public String getSkillInternalName() {
        return "WITHER AURA";
    }

    @Override
    public boolean execute(Player executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        Creature damager = executer.getMonsterEntity().getEntity();
        Location loc = damager.getLocation();
        return this.execute(executer, loc);
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {

        Creature damager = executer.getMonsterEntity().getEntity();
        CraftCreature cDamager = (CraftCreature) damager;
        World w = targetLoc.getWorld();

        for (Player p : w.getPlayers()) {
            if (p.getLocation().distanceSquared(targetLoc) <= 10 * 10) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 0), true);
            }
        }

        ParticleEffect effect = new ParticleEffect(Particle.PORTAL, 50, 0.5, 0.5, 0.5, 0.01);
        Vector vec = targetLoc.toVector();
        vec.setY(vec.getY() + 0.5);
        PointEffect.doEffect(w, vec, effect);

        SphereEffect.doEffect(targetLoc.clone().add(0, 1, 0), Particle.SPELL_WITCH, 10, 0, 0, 0, 150, 10);

        return true;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 10;
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
        return false;
    }

}
