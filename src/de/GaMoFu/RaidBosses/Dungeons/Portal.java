package de.GaMoFu.RaidBosses.Dungeons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.GaMoFu.RaidBosses.ParticleEffect;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Monsters.Monster;
import de.GaMoFu.RaidBosses.Monsters.MonsterType;
import de.GaMoFu.RaidBosses.ParticleEffects.CubeEffect;
import de.GaMoFu.RaidBosses.ParticleEffects.PointEffect;
import de.GaMoFu.RaidBosses.ParticleEffects.RingEffect;

public class Portal implements Listener {

    private RaidBosses plugin;

    private ArmorStand damageIndicator;

    private Location loc;

    private Dungeon dungeon;

    private int particleTmr;
    private int particleFlootTmr;

    private int spawnDelayTmr;
    private int spawnDelayTicks;

    private ParticleEffect spawnEffect;

    private ParticleEffect floorParticle;

    private List<Monster<? extends Creature>> spawnedMonsters;

    private List<MonsterType> spawnType;

    private int spawnPerTick;

    private int maxSpawns;

    private double maxHealth;

    private double health;

    private Random rnd;

    public Portal(RaidBosses plugin, Dungeon dungeon, Location loc, double maxHealth, int spawnDelayTicks,
            int spawnPerTick, int maxSpawns, List<MonsterType> spawnTypes) {
        this.plugin = plugin;
        // this.dungeon = dungeon;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.maxHealth = maxHealth;
        this.loc = loc;

        this.dungeon = dungeon;

        this.spawnDelayTicks = spawnDelayTicks;

        this.spawnPerTick = spawnPerTick;
        this.maxSpawns = maxSpawns;
        this.spawnType = new ArrayList<>(spawnTypes);

        this.spawnedMonsters = new LinkedList<>();

        this.spawnEffect = new ParticleEffect(Particle.PORTAL, 20, 0.5, 1, 0.5, 0.2);
        this.floorParticle = new ParticleEffect(Particle.PORTAL, 50, 0.5, 0.5, 0.5, 0.01);

        this.rnd = new Random();
    }

    public void onSpawn() {
        this.health = maxHealth;

        this.damageIndicator = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        this.damageIndicator.setVisible(false);

        this.particleTmr = 0;
        this.particleFlootTmr = 0;

        this.spawnDelayTmr = 0;

        for (int i = 1; i <= 5; i++) {
            final int radius = i;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    RingEffect.doEffect(getLocation(), Particle.SPELL_WITCH, 100, 0.5f, 0.5f, 0, 0, 20, radius);
                }
            }, i * 3);
        }
    }

    public Location getLocation() {
        return this.loc.clone();
    }

    public void onTick() {

        this.particleTmr++;
        if (this.particleTmr >= 5) {
            this.particleTmr = 0;

            CubeEffect.doEffect(this.loc.clone().add(0, 1, 0), Particle.SPELL_WITCH, 2, 0, 0, 0, 0.5, 1, 0.5, 0, 30);
        }

        this.particleFlootTmr++;
        if (this.particleFlootTmr >= 20) {
            this.particleFlootTmr = 0;

            PointEffect.doEffect(this.loc.getWorld(), getLocation().add(0, 0.5, 0).toVector(), this.floorParticle);
        }

        this.spawnDelayTmr++;
        if (this.spawnDelayTmr >= this.spawnDelayTicks) {
            this.spawnDelayTmr = 0;

            if (this.spawnedMonsters.size() < this.maxSpawns) {
                // do not "verletzen" maxSpawns
                for (int i = 0; i < Math.min(this.spawnPerTick, this.maxSpawns - this.spawnedMonsters.size()); i++) {
                    double r = 0.5 + 2 * rnd.nextDouble();
                    double phi = 2 * Math.PI * rnd.nextDouble();

                    int rndTypeIndex = rnd.nextInt(this.spawnType.size());
                    MonsterType spawnType = this.spawnType.get(rndTypeIndex);

                    Location spawnLoc = this.loc.clone();
                    spawnLoc.add(r * Math.cos(phi), 0, r * Math.sin(phi));

                    SpawnedMonster spawnedMonster = dungeon.addCustomSpawnedMonster(spawnType, spawnLoc);
                    this.spawnedMonsters.add(spawnedMonster.getMonsterEntity());

                    PointEffect.doEffect(loc.getWorld(), spawnLoc.clone().add(0, 1, 0).toVector(), spawnEffect);
                    loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_AMBIENT, 1, 0.5f);
                }
            }
        }

    }

    public void onEnd() {
        this.damageIndicator.remove();

        this.loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_DEATH, 1, 1.5f);

        for (Monster<? extends Creature> m : this.spawnedMonsters) {
            dungeon.removeCustomSpawnedMonster(m.getEntity().getUniqueId());
            m.getEntity().remove();
        }
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getUniqueId().equals(this.damageIndicator.getUniqueId())) {
            return;
        }

        // only player can damage the portal
        if (event.getDamager() instanceof Projectile) {
            Projectile p = (Projectile) event.getDamager();
            if (!(p.getShooter() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        } else if (!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        this.loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_HURT, 1, 0.5f);
        this.health -= event.getFinalDamage();

    }
}
