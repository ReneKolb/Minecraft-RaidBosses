package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;

public class FallingSwordHandler implements Listener {

    private static final double DAMAGE_DIST_SQUARED = 1;

    public class FallingSword {

        public ArmorStand entity;

        public double damage;

        public Entity spawner;

    }

    private Map<UUID, FallingSword> spawnedFallingSwords;

    private RaidBosses plugin;

    private Random rnd = new Random();

    public FallingSwordHandler(RaidBosses plugin) {
        this.plugin = plugin;

        this.spawnedFallingSwords = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this.damageChecker, 2, 2);
    }

    private Runnable damageChecker = new Runnable() {

        @Override
        public void run() {
            List<UUID> toRemove = new LinkedList<>();
            for (FallingSword fs : spawnedFallingSwords.values()) {
                for (LivingEntity le : fs.entity.getWorld().getLivingEntities()) {
                    if (fs.entity.getUniqueId().equals(le.getUniqueId()))
                        continue;

                    if ((fs.spawner instanceof Player) && (le instanceof Player))
                        continue;

                    if (plugin.getHologramHandler().isHologramEntity(le))
                        continue;

                    if (le.getEyeLocation().distanceSquared(fs.entity.getLocation()) <= DAMAGE_DIST_SQUARED) {
                        if (fs.spawner instanceof Player) {
                            // trigger events and so
                            EntityHuman human = ((CraftPlayer) fs.spawner).getHandle();
                            ((CraftLivingEntity) le).getHandle().damageEntity(DamageSource.playerAttack(human),
                                    (float) fs.damage);
                        } else {
                            // trigger events and so
                            EntityLiving entityLiving = ((CraftLivingEntity) fs.spawner).getHandle();
                            ((CraftLivingEntity) le).getHandle().damageEntity(DamageSource.mobAttack(entityLiving),
                                    (float) fs.damage);
                        }

                        toRemove.add(fs.entity.getUniqueId());

                        // only one target per sword
                        break;
                    }
                }
            }

            for (UUID id : toRemove) {
                scheduleRemove(spawnedFallingSwords.get(id));
            }
        }

    };

    public void spawn(Location loc, Material itemType, Entity spawner, double damage) {
        double x = 1.3963;// Math.PI*80.0/180.0;
        double y = 1.3963;// Math.PI*80.0/180.0;
        double z = 0;

        ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        a.setItemInHand(new ItemStack(itemType));
        a.setVisible(false);
        a.setRightArmPose(new EulerAngle(x, y, z));

        FallingSword fs = new FallingSword();
        fs.entity = a;
        fs.damage = damage;
        fs.spawner = spawner;

        this.spawnedFallingSwords.put(a.getUniqueId(), fs);
    }

    public void spawnInArea(Location center, float radius, int amount, Material itemType, Entity spawner, double damage,
            int delayTicks) {

        double dx, dz;
        for (int i = 0; i < amount; i++) {

            do {
                dx = 2 * radius * rnd.nextDouble() - radius;
                dz = 2 * radius * rnd.nextDouble() - radius;
            } while ((dx * dx + dz * dz) > radius * radius);

            final Location targetLoc = center.clone().add(dx, 0, dz);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {

                @Override
                public void run() {
                    spawn(targetLoc, itemType, spawner, damage);
                }

            }, i * delayTicks);
        }
    }

    private void scheduleRemove(FallingSword fallingSword) {
        fallingSword.entity.remove();
        this.spawnedFallingSwords.remove(fallingSword.entity.getUniqueId());
    }

    // @EventHandler
    // public void handleSwordHit(EntityDamageByEntityEvent event) {
    // System.out.println("ent by ent: " + event.getDamager().getType() + " -> " +
    // event.getEntity().getType());
    // if (!this.spawnedFallingSwords.containsKey(event.getDamager().getUniqueId()))
    // {
    // return;
    // }
    //
    // FallingSword fs =
    // this.spawnedFallingSwords.get(event.getDamager().getUniqueId());
    //
    // if (fs.spawner != null) {
    // if ((fs.spawner instanceof Player) && (event.getEntity() instanceof Player))
    // {
    // scheduleRemove(fs);
    // event.setCancelled(true);
    // return;
    // }
    // }
    //
    // event.setDamage(fs.damage);
    // scheduleRemove(fs);
    // }

    @EventHandler
    public void handleSwordLand(EntityDamageEvent event) {
        if (!this.spawnedFallingSwords.containsKey(event.getEntity().getUniqueId())) {
            return;
        }
        FallingSword fs = this.spawnedFallingSwords.get(event.getEntity().getUniqueId());

        if (event.getCause() == DamageCause.FALL) {
            scheduleRemove(fs);
        }
    }

    @EventHandler
    public void handleSwordDeath(EntityDeathEvent event) {
        FallingSword fs = this.spawnedFallingSwords.get(event.getEntity().getUniqueId());
        if (fs != null) {
            scheduleRemove(fs);
        }
    }

}
