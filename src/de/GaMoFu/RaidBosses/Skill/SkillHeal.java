package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;

public abstract class SkillHeal implements ISkill {

    protected abstract int getLevel();

    protected abstract double getHealAmount();

    @Override
    public String getSkillDisplayName() {
        return ChatColor.AQUA + "Heal Lv." + getLevel();
    }

    @Override
    public String getSkillInternalName() {
        return "HEAL_LVL_" + getLevel();
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
    public List<String> getLore() {
        return Arrays.asList(ChatColor.GRAY + "Heal yourself by",
                "  " + ChatColor.WHITE + RaidBosses.df.format(getHealAmount()) + ChatColor.RED + " Health", "",
                ChatColor.DARK_GRAY + "Cost: " + ChatColor.BLUE + RaidBosses.df.format(getBasicHungerCost()));
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
