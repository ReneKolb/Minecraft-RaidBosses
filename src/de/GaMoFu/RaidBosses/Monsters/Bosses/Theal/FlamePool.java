package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.GaMoFu.RaidBosses.AreaCloudSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class FlamePool implements ISkill {

    public static final double MAX_POOL_SPAWN_RANGE = 15;
    public static final int DURATION_TICKS = 200;
    public static final int DAMAGE_DELAY_TICKS = 20 * 1; // 1sec

    private Random rnd = new Random();

    private FlameAuraDamage aura;

    public FlamePool(FlameAuraDamage aura) {
        this.aura = aura;
    }

    @Override
    public String getSkillDisplayName() {
        return "Flame pool";
    }

    @Override
    public String getSkillInternalName() {
        return "FLAME POOL";
    }

    @Override
    public boolean execute(Player executer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        double r = MAX_POOL_SPAWN_RANGE * rnd.nextDouble();
        double phi = 2 * Math.PI * rnd.nextDouble();

        Location loc = executer.getMonsterEntity().getEntity().getLocation().add(r * Math.cos(phi), 0,
                r * Math.sin(phi));

        return this.execute(executer, loc);
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        Creature c = executer.getMonsterEntity().getEntity();
        World w = c.getWorld();

        AreaEffectCloud cloud = (AreaEffectCloud) w.spawnEntity(targetLoc, EntityType.AREA_EFFECT_CLOUD);
        cloud.setBasePotionData(new PotionData(PotionType.WATER)); // no effect

        // cloud.setColor(Color.fromRGB(50, 50, 255)); // =00ffb7
        cloud.setParticle(Particle.LAVA);
        cloud.setDuration(DURATION_TICKS);
        cloud.setRadius(1); // just a smalle center. the damage effect will be a bigger range
        cloud.setRadiusOnUse(0); // dont decrease the radius on use
        cloud.setRadiusPerTick(0); // dont decrease the radius with every rick
        cloud.setReapplicationDelay(DAMAGE_DELAY_TICKS);
        cloud.setSource(c);
        cloud.setWaitTime(0); // instantly show particles

        AreaCloudSettings settings = new AreaCloudSettings(0, 0, 0, 0);
        settings.setOnTickSkill(aura, executer, targetLoc);
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
    public List<String> getLore() {
        return null;
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
