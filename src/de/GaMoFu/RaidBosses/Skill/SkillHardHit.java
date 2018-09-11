package de.GaMoFu.RaidBosses.Skill;

import java.util.SortedSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Items.Item;
import de.GaMoFu.RaidBosses.Skill.Tooltip.CooldownLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DamageLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HorizontalLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerCostLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class SkillHardHit implements ISkill {

    public abstract float getKnockbackMulti();

    public abstract float getAttackRange();

    public abstract double getDamage();

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Hard Hit";
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
        ItemStack stack = executer.getInventory().getItemInMainHand();

        // Resulting damage is WeaponDamage + SkillDamage
        double damage = Item.getItemStackDamage(stack);
        damage += getDamage();

        SortedSet<Distance<LivingEntity>> targets = getEntitiesInFront(executer, true, getAttackRange());
        for (Distance<LivingEntity> le : targets) {
            applyKnockback(le.getObject(),
                    le.getObject().getLocation().toVector().subtract(executer.getLocation().toVector()));
            le.getObject().damage(damage, executer);
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
    public SkillTooltipBuilder getTooltipBuilder() {

        // TODO:
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("TODO"))
                .add(new EmptyLine())
                .add(new DamageLine(getDamage()))
                // .add(new HealPerSecLine(0.2, 50))
                // .add(new DurationLine(20 * 12))
                // .add(new RadiusLine(getHealingRange()))

                .add(new HorizontalLine())
                .add(new HungerCostLine(getBasicHungerCost()))
                .add(new CooldownLine(getCooldownTicks()));
        //@formatter:on
    }

    // @Override
    // public List<String> getLore() {
    // return Arrays.asList("Hard Hit");
    // }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
