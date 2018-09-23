package de.GaMoFu.RaidBosses.Skill;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.CooldownLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HorizontalLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerCostLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.InstantHealLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.RangeLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class Healing implements ISkill {

    public abstract double getHealAmount();

    public float getMaxRange() {
        return 7;
    }

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Healing";
    }

    @Override
    public boolean execute(Player executer) {

        PlayerSettings ps = RaidBosses.getPluginInstance().getPlayerSettings(executer);

        Player closestPlayer = null;
        double closestRange = Double.MAX_VALUE;

        for (Player p : executer.getWorld().getPlayers()) {
            if (p.getUniqueId().equals(executer.getUniqueId())) {
                continue;
            }

            double dist = p.getLocation().distance(executer.getLocation());
            if (dist <= getMaxRange() && dist < closestRange && ps.looksAt(p, true)) {
                double maxHP = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (p.getHealth() >= maxHP) {
                    // The player has max HP, so he does not need healing
                    continue;
                }

                closestRange = dist;
                closestPlayer = p;
            }
        }

        if (closestPlayer == null) {
            executer.sendMessage("No target that needs healing");
            return false;
        }

        double maxHealth = closestPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = closestPlayer.getHealth();

        closestPlayer.setHealth(Math.min(maxHealth, currentHealth + getHealAmount()));
        Bukkit.getServer().getPluginManager()
                .callEvent(new EntityRegainHealthEvent(closestPlayer, getHealAmount(), RegainReason.MAGIC));

        executer.getWorld().playSound(closestPlayer.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);
        executer.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, closestPlayer.getEyeLocation(), 7, 0.6, 1, 0.6);

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 2;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 4;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.ROSE_RED;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("The targeted player is healed by the inner power of the potato."))
                .add(new EmptyLine())
                .add(new InstantHealLine(getHealAmount()))
                .add(new RangeLine(getMaxRange()))

                .add(new HorizontalLine())
                .add(new HungerCostLine(getBasicHungerCost()))
                .add(new CooldownLine(getCooldownTicks()));
        //@formatter:on
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
