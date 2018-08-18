package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.ParticleEffects.RingEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.EntityHuman;

public class FrostAuraDamage implements ISkill {

    private boolean completeFreeze;

    public FrostAuraDamage(boolean completeFreeze) {
        this.completeFreeze = completeFreeze;
    }

    @Override
    public int getLevel() {
        return -1;
    }
    
    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Frost Aura";
    }

    @Override
    public String getSkillInternalName() {
        return "FROST_AURA";
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
        System.out.println("Execute FrostAura");
        Creature damager = executer.getMonsterEntity().getEntity();
        CraftCreature cDamager = (CraftCreature) damager;
        World w = targetLoc.getWorld();

        // TODO: fake bottom block change to ice or packed_ice. noise=0.3, radius=8,
        // duration=440

        DamageSource source = DamageSource.mobAttack(cDamager.getHandle());
        // Utils.setIgnoreArmor(source);

        for (Player p : w.getPlayers()) {
            if (p.getLocation().distanceSquared(targetLoc) <= 8 * 8) {

                EntityHuman human = ((CraftPlayer) p).getHandle();
                // d�mlicher Umweg, aber so werden wenigstens alle Events richtig getriggert
                human.damageEntity(source, 5);

                PlayerSettings ps = RaidBosses.getPluginInstance().getPlayerSettings(p);
                // Prevent FOV zoom shit... noooott ach is doch kacke... aber zumindest hat man
                // so mehr kontrolle �ber den genauen wert
                if (completeFreeze) {
                    ps.addSlowness(-1, Operation.MULTIPLY_SCALAR_1, 4 * 20);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 4 * 20, 128)); // disallow jump
                } else {
                    // ps.addSlowness(-3 * 0.15, Operation.MULTIPLY_SCALAR_1, 200);
                    // This way, to show the potion effect on player's HUD
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 4));
                }
            }
        }

        w.playSound(targetLoc, Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 0.5f);
        for (int i = 1; i <= 8; i++) {
            final int radius = i;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                @Override
                public void run() {
                    RingEffect.doEffect(targetLoc, Particle.WATER_SPLASH, 100, 0.5f, 0.5f, 0, 0, 20, radius);
                    RingEffect.doEffect(targetLoc, Particle.SNOWBALL, 20, 0.5f, 0.5f, 0, 0, 20, radius);
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
