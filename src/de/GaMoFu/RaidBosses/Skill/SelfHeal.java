package de.GaMoFu.RaidBosses.Skill;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.CooldownLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HorizontalLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerCostLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.InstantHealLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class SelfHeal implements ISkill {

    protected abstract double getHealAmount();

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Self Heal";
    }

    @Override
    public boolean execute(Player executer) {
        double maxHealth = executer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = executer.getHealth();

        if (currentHealth >= maxHealth) {
            return false;
        }

        executer.setHealth(Math.min(maxHealth, currentHealth + getHealAmount()));
        Bukkit.getServer().getPluginManager()
                .callEvent(new EntityRegainHealthEvent(executer, getHealAmount(), RegainReason.MAGIC));

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        Creature c = executer.getMonsterEntity().getEntity();
        double maxHealth = c.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = c.getHealth();

        if (currentHealth >= maxHealth) {
            return false;
        }

        c.setHealth(Math.min(maxHealth, currentHealth + getHealAmount()));
        Bukkit.getServer().getPluginManager()
                .callEvent(new EntityRegainHealthEvent(c, getHealAmount(), RegainReason.MAGIC));

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("Heal at Location is currently not supported");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 4;
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
    public short getDisplayDurability() {
        return 0;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("The player is healed by the inner power of the potato."))
                .add(new EmptyLine())
                .add(new InstantHealLine(getHealAmount()))

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
