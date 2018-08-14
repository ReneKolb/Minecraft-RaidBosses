package de.GaMoFu.RaidBosses;

import java.util.Random;

import org.bukkit.Location;

import de.GaMoFu.RaidBosses.Config.IdleWalkStrollSettings;

public class IdleWalkStroll extends IdleWalk<IdleWalkStrollSettings> {

    private transient int strollTimer;

    private transient Random rnd;

    public IdleWalkStroll() {
        super();

        this.rnd = new Random();

        this.strollTimer = 0;
    }

    public void loop() {

        this.strollTimer--;
        if (this.strollTimer <= 0) {
            this.strollTimer = this.idleWalkSettings.getMinStrollDelay() + rnd
                    .nextInt(this.idleWalkSettings.getMaxStrollDelay() - this.idleWalkSettings.getMinStrollDelay() + 1);

            double r = rnd.nextDouble() * this.idleWalkSettings.getMaxStrollRadius();
            double phi = rnd.nextDouble() * 2 * Math.PI;

            double dx = r * Math.cos(phi);
            double dz = r * Math.sin(phi);

            Location spawnedLoc = this.monster.getSpawnedLocation();
            double x = spawnedLoc.getX() + dx;
            double y = spawnedLoc.getY();
            double z = spawnedLoc.getZ() + dz;

            double speed = 1d;// getCraftCreature().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();

            getCraftCreature().getHandle().getNavigation().a(x, y, z, speed);
        }

    }

}
