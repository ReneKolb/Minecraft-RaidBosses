package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffects.PointEffect;

public class ProjectileManager implements Listener {

    private HashMap<UUID, CustomProjectile> launchedProjectiles;

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

    public void addCustomDamageProjectile(Projectile projectile, double damageOnHit, List<ParticleEffect> onHit) {
        CustomProjectile cp = new CustomProjectile(damageOnHit, false, projectile, false, onHit);

        this.launchedProjectiles.put(cp.getUuid(), cp);
    }

    @EventHandler
    public void onDamageEntityByEntity(EntityDamageByEntityEvent event) {
        UUID damagerID = event.getDamager().getUniqueId();
        if (!this.launchedProjectiles.containsKey(damagerID)) {
            return;
        }

        CustomProjectile projectile = this.launchedProjectiles.get(damagerID);
        event.setDamage(projectile.getDamage());

        for (ParticleEffect e : projectile.getOnHit()) {
            PointEffect.doEffect(event.getDamager().getWorld(), event.getDamager().getLocation().toVector(), e);
        }
    }

    @EventHandler
    public void onBlockIgnote(BlockIgniteEvent event) {
        // Prevent Custom Fireballs from igniting hit Blocks
        UUID igniterID = event.getIgnitingEntity().getUniqueId();
        if (!this.launchedProjectiles.containsKey(igniterID)) {
            return;
        }
        CustomProjectile projectile = this.launchedProjectiles.get(igniterID);

        if (!projectile.isCanIgnoteBlocks()) {
            event.setCancelled(true);
        }
    }

    private Runnable loop = new Runnable() {

        @Override
        public void run() {
            List<UUID> toRemove = new LinkedList<>();
            for (Map.Entry<UUID, CustomProjectile> e : launchedProjectiles.entrySet()) {
                CustomProjectile p = e.getValue();

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
