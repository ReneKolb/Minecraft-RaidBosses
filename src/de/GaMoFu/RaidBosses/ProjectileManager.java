package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ProjectileManager implements Listener {

    private HashMap<UUID, ParticleProjectile> launchedProjectiles;

    private RaidBosses plugin;

    public ProjectileManager(RaidBosses plugin) {
        this.plugin = plugin;
        this.launchedProjectiles = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, loop, 1, 1);
    }

    public void launchProjectile(List<ParticleEffect> onFlyingTick, List<ParticleEffect> onHit, float hitRadius,
            double maxFlyDistance, double damage, boolean ignoreArmor, LivingEntity shooter, Vector dir,
            Location location) {
        ParticleProjectile pp = new ParticleProjectile(onFlyingTick, onHit, hitRadius, maxFlyDistance, damage,
                ignoreArmor, shooter, dir, location);

        this.launchedProjectiles.put(pp.getUuid(), pp);
    }

    public void launchAimingProjectile(List<ParticleEffect> onFlyingTick, List<ParticleEffect> onHit, float hitRadius,
            double maxFlyDistance, double damage, boolean ignoreArmor, LivingEntity shooter, Location location,
            double speed, double maxChangeAnglePerTick, LivingEntity target) {
        ParticleProjectile pp = new AimingParticleProjectile(onFlyingTick, onHit, hitRadius, maxFlyDistance, damage,
                ignoreArmor, shooter, location, speed, maxChangeAnglePerTick, target);

        this.launchedProjectiles.put(pp.getUuid(), pp);

    }

    private Runnable loop = new Runnable() {

        @Override
        public void run() {
            List<UUID> toRemove = new LinkedList<>();
            for (Map.Entry<UUID, ParticleProjectile> e : launchedProjectiles.entrySet()) {
                ParticleProjectile p = e.getValue();

                p.tick();

                if (p.isDie()) {
                    toRemove.add(e.getKey());
                }
            }

            for (UUID id : toRemove) {
                launchedProjectiles.remove(id);
            }

        }
    };

}
