package de.GaMoFu.RaidBosses.Skill;

import java.util.Arrays;
import java.util.List;

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

public abstract class GroupHealing implements ISkill {

    protected abstract int getLevel();

    public abstract double getHealingAmount();

    public abstract float getHealingRange();

    @Override
    public String getSkillDisplayName() {
        return "Group Healing Lv." + getLevel();
    }

    @Override
    public String getSkillInternalName() {
        return "GROUP_HEALING_LVL_1";
    }

    @Override
    public boolean execute(Player executer) {
        World w = executer.getWorld();

        AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(executer.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

        // Healing per level (lvl1 = 0) in ticks per half Heart (Heart per sec):
        // 1->50(0.2), 2->25(0.4), 3->12(0.8), 4->6(1.66), 5->3(3.33), 6+->1(10)
        cloud.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 25 + 1, 1/* 1 = lvl 2 */), true); // override

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
    public List<String> getLore() {
        return Arrays.asList("Heal near players");
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
