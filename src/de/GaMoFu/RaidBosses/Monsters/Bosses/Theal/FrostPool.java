package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.GaMoFu.RaidBosses.AreaCloudSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class FrostPool implements ISkill {

    public static final int DAMAGE_DELAY_TICKS = 5 * 8; // 2sec (40 ticks). should be a multiple of 5 since the
                                                        // AreaCloudEvent is
                                                        // triggered every 5 ticks
    public static final float DAMAGE_RANGE = 8;
    public static final double MAX_SPAWN_POOL_RANGE = 15;

    private FrostAuraDamage aura;

    public FrostPool(FrostAuraDamage referenceToFrostAuraDamage) {
        this.aura = referenceToFrostAuraDamage;
    }

    @Override
    public int getLevel() {
        return -1;
    }
    
    @Override
    public String getSkillDisplayNameWithoutLevel() {
        return "Frost Pool";
    }

    @Override
    public String getSkillInternalName() {
        return "FROST_POOL";
    }

    @Override
    public boolean execute(Player executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {

        UUID randomTarget = executer.getMonsterEntity().getRandomAggroTarget();

        for (LivingEntity le : executer.getMonsterEntity().getEntity().getWorld().getLivingEntities()) {
            if (le.getUniqueId().equals(randomTarget)) {
                return this.execute(executer, le.getLocation());
            }
        }

        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
//        System.out.println("Execute Frost Pool");
        // cd=400, r=8
        Creature c = executer.getMonsterEntity().getEntity();
        World w = c.getWorld();

        AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(targetLoc, EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

        cloud.setColor(Color.fromRGB(50, 50, 255)); // =00ffb7
        cloud.setDuration(DAMAGE_DELAY_TICKS);
        cloud.setRadius(DAMAGE_RANGE);
        cloud.setRadiusOnUse(0); // dont decrease the radius on use
        cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
        cloud.setReapplicationDelay(DAMAGE_DELAY_TICKS);
        cloud.setSource(c);
        cloud.setWaitTime(0); // instantly show particles

        AreaCloudSettings settings = new AreaCloudSettings(0, 0, 0, 0);
        settings.setOnEndSkill(aura, executer, targetLoc);
        RaidBosses.getPluginInstance().getAreaCloudHandler().handleCustomAreaEffectCloud(cloud, settings);

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
