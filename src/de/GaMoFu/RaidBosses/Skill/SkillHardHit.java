package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.SpawnedMonster;

public abstract class SkillHardHit implements ISkill {

    public abstract int getLevel();

    public abstract float getKnockbackMulti();

    public abstract float getAttackRange();

    public abstract double getDamage();

    @Override
    public String getSkillDisplayName() {
        return "Hard Hit Lv." + getLevel();
    }

    @Override
    public String getSkillInternalName() {
        return "HARD_HIT_LVL_" + getLevel();
    }

    private void applyKnockback(LivingEntity target, Vector dir) {
        Vector finalDir = dir.clone().normalize();
        finalDir = finalDir.setY(0.35); // y angle to top
        finalDir = finalDir.multiply(getKnockbackMulti());
        target.setVelocity(finalDir);
    }

    @Override
    public boolean execute(Player executer) {
        SortedSet<Distance<LivingEntity>> targets = getEntitiesInFront(executer, true, getAttackRange());
        for (Distance<LivingEntity> le : targets) {
            applyKnockback(le.getObject(),
                    le.getObject().getLocation().toVector().subtract(executer.getLocation().toVector()));
            le.getObject().damage(getDamage(), executer);
        }

        if (!targets.isEmpty()) {
            executer.getWorld().playSound(executer.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1f);
            return true;
        }

        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        Creature c = executer.getMonsterEntity().getEntity();

        SortedSet<Distance<LivingEntity>> targets = getEntitiesInFront(c, false, getAttackRange());
        for (Distance<LivingEntity> le : targets) {
            applyKnockback(le.getObject(),
                    le.getObject().getLocation().toVector().subtract(c.getLocation().toVector()));
            le.getObject().damage(getDamage(), c);
        }
        if (!targets.isEmpty()) {
            c.getWorld().playSound(c.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1f);
            return true;
        }

        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("HardHit at Location is currently not supported");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 6;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 8;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.ANVIL;
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("Hard Hit");
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
