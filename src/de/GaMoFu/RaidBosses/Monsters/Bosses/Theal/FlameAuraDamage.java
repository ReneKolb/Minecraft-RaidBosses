package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.ParticleEffects.RingEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityHuman;

public class FlameAuraDamage implements ISkill {

    @Override
    public int getLevel() {
        return -1;
    }

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Flame Aura";
    }

    @Override
    public String getSkillInternalName() {
        return "FLAME AURA";
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

        DamageSource source = DamageSource.mobAttack(cDamager.getHandle());
        // Utils.setIgnoreArmor(source);

        for (Player p : w.getPlayers()) {
            if (p.getLocation().distanceSquared(targetLoc) <= 8 * 8) {

                EntityHuman human = ((CraftPlayer) p).getHandle();
                // d�mlicher Umweg, aber so werden wenigstens alle Events richtig getriggert
                human.damageEntity(source, 5);
            }
        }

        w.playSound(targetLoc, Sound.BLOCK_FIRE_AMBIENT, 1, 1);
        for (int i = 1; i <= 8; i++) {
            final int radius = i;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                @Override
                public void run() {
                    RingEffect.doEffect(targetLoc, Particle.FLAME, 100, 0.5f, 0.5f, 0, 0, 20, radius);
                }

            }, 3 * i);
        }

        return true;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 400;
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
