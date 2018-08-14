package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import net.minecraft.server.v1_13_R1.EntityWitherSkull;

public class DarkShot implements ISkill {

    @Override
    public String getSkillDisplayName() {
        return "Dark Shot";
    }

    @Override
    public String getSkillInternalName() {
        return "DARK SHOT";
    }

    @Override
    public boolean execute(Player executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        Creature c = executer.getMonsterEntity().getEntity();
        World w = c.getWorld();

        // Get random target
        UUID targetID = executer.getMonsterEntity().getRandomAggroTarget();

        LivingEntity target = null;
        for (LivingEntity le : w.getLivingEntities()) {
            if (le.getUniqueId().equals(targetID)) {
                target = le;
                break;
            }
        }

        if (target == null) {
            // Handle if no target?
            System.out.println("No target. abort FrostBolt");
            return false;
        }

        return this.execute(executer, target.getEyeLocation());
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {
        Creature c = executer.getMonsterEntity().getEntity();
        CraftCreature cc = (CraftCreature) c;
        World w = c.getWorld();
        CraftWorld cw = (CraftWorld) w;

        Location location = executer.getMonsterEntity().getEntity().getEyeLocation();

        Vector dir = targetLoc.clone().subtract(c.getEyeLocation()).toVector();
        dir.normalize();
        // dir.multiply(0.8);
        Vector velo = dir.clone().multiply(0.2);
        double len = dir.length();

        EntityWitherSkull launch = new EntityWitherSkull(cw.getHandle(), cc.getHandle(), dir.getX(), dir.getY(),
                dir.getZ());

        launch.dirX = velo.getX();
        launch.dirY = velo.getY();
        launch.dirZ = velo.getZ();

        launch.projectileSource = cc;
        launch.setPositionRotation(location.getX(), location.getY(), location.getZ(),
                (float) Math.atan2(dir.getX(), dir.getZ()), (float) Math.asin(dir.getY() / len));

        cw.getHandle().addEntity(launch);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

            @Override
            public void run() {
                EntityWitherSkull launch = new EntityWitherSkull(cw.getHandle(), cc.getHandle(), dir.getX(), dir.getY(),
                        dir.getZ());

                launch.dirX = velo.getX();
                launch.dirY = velo.getY();
                launch.dirZ = velo.getZ();

                launch.projectileSource = cc;
                launch.setPositionRotation(location.getX(), location.getY(), location.getZ(),
                        (float) Math.atan2(dir.getX(), dir.getZ()), (float) Math.asin(dir.getY() / len));

                // launch.getBukkitEntity().setVelocity(velo); // ok?
                cw.getHandle().addEntity(launch);
            }

        }, 10);

        // RaidBosses.getPluginInstance().getCustomDamageHandler().setCustomDamageToEntity(ws.getUniqueId(),
        // damage);

        return true;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 40;
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
