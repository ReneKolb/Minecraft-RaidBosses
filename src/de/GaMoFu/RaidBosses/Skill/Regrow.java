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

public abstract class Regrow implements ISkill{

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Regrow";
    }

    @Override
    public String getSkillInternalName() {
        return "REGROW_LVL_"+getLevel();
    }
    
    public abstract int getEffectDurationTicks();
    
    public abstract float getRadius();

    @Override
    public boolean execute(Player executer) {
        World world = executer.getWorld();
        Location loc = executer.getLocation();
        
        AreaEffectCloud cloud = (AreaEffectCloud) world.spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect
        
        cloud.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, getEffectDurationTicks(), getLevel()-1), true); // override

        cloud.setColor(Color.fromRGB(0, 255, 204));
        cloud.setDuration(10);
        cloud.setRadius(getRadius());
        cloud.setRadiusOnUse(0); // dont decrease the radius on use
        cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
        cloud.setReapplicationDelay(Integer.MAX_VALUE); // only apply effect once
        cloud.setSource(executer);
        cloud.setWaitTime(0); // instantly apply effect

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
        return 20*4;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.GLOWSTONE_DUST;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("Let the wounded flesh regrow and heal a small amount of live per second"))
                .add(new EmptyLine())
                .add(new HealPerSecLine(1, (int) (50.0 / getLevel()))) // always heal 0.5 heart per (50 / level) seconds (with level from 1..inf)
                .add(new DurationLine(getEffectDurationTicks()))
                .add(new RadiusLine(getRadius()))

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
