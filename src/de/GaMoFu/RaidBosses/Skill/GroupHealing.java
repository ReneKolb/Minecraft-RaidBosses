package de.GaMoFu.RaidBosses.Skill;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.CooldownLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DurationLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HealPerSecLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HorizontalLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerCostLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.RadiusLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class GroupHealing implements ISkill {

    public abstract double getHealingAmount();

    public abstract float getHealingRange();

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Group Healing";
    }

    @Override
    public String getSkillInternalName() {
        return "GROUP_HEALING_LVL_" + getLevel();
    }

    @Override
    public boolean execute(Player executer) {
        World w = executer.getWorld();

        AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(executer.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

        // Healing per level (lvl1 = 0) in ticks per half Heart (Heart per sec):
        // 1->50(0.2), 2->25(0.4), 3->12(0.8), 4->6(1.66), 5->3(3.33), 6+->1(10)
        cloud.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 25 + 1, getLevel()/* 1 = lvl 2 */), true); // override

        cloud.setColor(Color.fromRGB(0, 255, 183)); // =00ffb7
        cloud.setDuration(20 * 12);
        cloud.setRadius(getHealingRange());
        cloud.setRadiusOnUse(0); // dont decrease the radius on use
        cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
        cloud.setReapplicationDelay(25);
        cloud.setSource(executer);
        cloud.setWaitTime(0); // instantly apply effect

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        System.out.println("Group healing is currentyl not supported for monsters");
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("Group healing is currentyl not supported for monsters");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 5;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 6;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.GLOWSTONE_DUST;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("Nearby players are healed by the inner power of the BIG potato."))
                .add(new EmptyLine())
                .add(new HealPerSecLine(1, (int) (50.0 / (getLevel() + 1)))) // always heal 0.5 heart per (50 / level) seconds (with level from 1..inf)
                .add(new DurationLine(20 * 12))
                .add(new RadiusLine(getHealingRange()))

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
