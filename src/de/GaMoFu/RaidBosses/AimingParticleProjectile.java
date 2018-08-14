package de.GaMoFu.RaidBosses;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class AimingParticleProjectile extends ParticleProjectile {

    private LivingEntity target;

    private double speed;

    private double maxChangeAnglePerTick;

    private Vector lastDir;

    public AimingParticleProjectile(List<ParticleEffect> onFlyingTick, List<ParticleEffect> onHit, float hitRadius,
            double maxFlyDistance, double damage, boolean ignoreArmor, LivingEntity shooter, Location location,
            double speed, double maxChangeAnglePerTick, LivingEntity target) {
        super(onFlyingTick, onHit, hitRadius, maxFlyDistance, damage, ignoreArmor, shooter, null, location);
        this.target = target;
        this.speed = speed;

        this.lastDir = location.getDirection().normalize().multiply(speed);
        // transform deg to rad
        this.maxChangeAnglePerTick = Math.PI * maxChangeAnglePerTick / 180.0;
    }

    @Override
    public Vector getDirectionVector() {
        Vector dir = this.target.getEyeLocation().subtract(this.getLocation()).toVector();

        if (dir.angle(lastDir) > this.maxChangeAnglePerTick) {

            Vector rotVec = lastDir.clone();
            rotVec.crossProduct(dir);
            rotVec = rotVec.normalize();

            // last direction
            double lx = lastDir.getX();
            double ly = lastDir.getY();
            double lz = lastDir.getZ();

            // (normal) rotation vector
            double ux = rotVec.getX();
            double uy = rotVec.getY();
            double uz = rotVec.getZ();

            // calc target dir
            double cos = Math.cos(maxChangeAnglePerTick);
            double sin = Math.sin(maxChangeAnglePerTick);

            // apply rotation matrix
            double tx = lx * (cos + ux * ux * (1 - cos)) + ly * (ux * uy * (1 - cos) - uz * sin)
                    + lz * (ux * uz * (1 - cos) + uy * sin);

            double ty = lx * (uy * ux * (1 - cos) + uz * sin) + ly * (cos + uy * uy * (1 - cos))
                    + lz * (uy * uz * (1 - cos) - ux * sin);

            double tz = lx * (uz * ux * (1 - cos) - uy * sin) + ly * (uz * uy * (1 - cos) + ux * sin)
                    + lz * (cos + uz * uz * (1 - cos));

            dir.setX(tx);
            dir.setY(ty);
            dir.setZ(tz);

        }

        dir.normalize();

        dir.multiply(speed);

        this.lastDir = dir.clone();

        return dir;
    }

}
