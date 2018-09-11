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

import de.GaMoFu.RaidBosses.AreaCloudSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.CooldownLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DescriptionLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.DurationLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.EmptyLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HorizontalLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerCostLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.HungerPerSecLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.RadiusLine;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class Refreshment implements ISkill {

    public abstract float getRange();

    public abstract int getDurationTicks();

    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Refreshment";
    }

    @Override
    public String getSkillInternalName() {
        return "REFRESHMENT_LVL_" + getLevel();
    }

    @Override
    public boolean execute(Player executer) {
        World w = executer.getWorld();

        // TODO: saturation is not increasing -.-
        AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(executer.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

        cloud.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 0), true); // override

        cloud.setColor(Color.fromRGB(99, 52, 14)); // =00ffb7
        cloud.setDuration(getDurationTicks());
        cloud.setRadius(getRange());
        cloud.setRadiusOnUse(0); // dont decrease the radius on use
        cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
        cloud.setReapplicationDelay(15);
        cloud.setSource(executer);
        cloud.setWaitTime(0); // instantly apply effect

        AreaCloudSettings settings = new AreaCloudSettings(0, 0, 1, 20);
        RaidBosses.getPluginInstance().getAreaCloudHandler().handleCustomAreaEffectCloud(cloud, settings);

        return true;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        System.out.println("Refreshment is currentyl not supported for monsters");
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        System.out.println("Refreshment is currentyl not supported for monsters");
        return false;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 60;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.MELON;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        //@formatter:off
        return new SkillTooltipBuilder()
                .add(new DescriptionLine("TODO"))
                .add(new EmptyLine())
                .add(new HungerPerSecLine(1, 20))
                .add(new DurationLine(getDurationTicks()))
                .add(new RadiusLine(getRange()))

                .add(new HorizontalLine())
                .add(new HungerCostLine(getBasicHungerCost()))
                .add(new CooldownLine(getCooldownTicks()));
        //@formatter:on
    }
    // @Override
    // public List<String> getLore() {
    // return Arrays.asList(ChatColor.GRAY + "Regenerates Hunger", " " +
    // ChatColor.WHITE + "1 " + ChatColor.DARK_RED
    // + " Hunger" + ChatColor.GRAY + " per " + ChatColor.WHITE + "1 " +
    // ChatColor.YELLOW + " sec");
    // }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
