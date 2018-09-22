package de.GaMoFu.RaidBosses;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Projectile;

public class CustomProjectile {

    private double damage;

    private boolean ignoreArmor;

    private UUID uuid;

    private Projectile parentProjectile;

    private boolean canIgnoteBlocks;
    
    // FIXME: maybe a patterned Effect
    private List<ParticleEffect> onHit;


    protected CustomProjectile(double damage, boolean ignoreArmor, UUID uuid, boolean canIgniteBlocks, List<ParticleEffect> onHit) {
        this.damage = damage;
        this.ignoreArmor = ignoreArmor;
        this.uuid = uuid;
        this.parentProjectile = null;
        this.canIgnoteBlocks = canIgniteBlocks;
        this.onHit = onHit;
        if (this.onHit == null) {
            this.onHit = Collections.emptyList();
        }
    }

    public CustomProjectile(double damage, boolean ignoreArmor, Projectile parentProjectile, boolean canIgniteBlocks, List<ParticleEffect> onHit) {
        this.damage = damage;
        this.ignoreArmor = ignoreArmor;
        this.uuid = parentProjectile.getUniqueId();
        this.parentProjectile = parentProjectile;
        this.canIgnoteBlocks = canIgniteBlocks;
        this.onHit = onHit;
        if (this.onHit == null) {
            this.onHit = Collections.emptyList();
        }
    }

    /**
     * @return the damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @return the ignoreArmor
     */
    public boolean isIgnoreArmor() {
        return ignoreArmor;
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return the canIgnoteBlocks
     */
    public boolean isCanIgnoteBlocks() {
        return canIgnoteBlocks;
    }

    public void tick() {

    }

    public boolean isDie() {
        return this.parentProjectile.isDead();
    }

    /**
     * @return the onHit
     */
    public List<ParticleEffect> getOnHit() {
        return onHit;
    }
    
}
