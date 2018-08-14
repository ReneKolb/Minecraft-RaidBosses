package de.GaMoFu.RaidBosses.ParticleEffects;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class SphereEffect {

    private static Random rnd = new Random();

    public static void doEffect(Location loc, Particle type, int amount, float verticalSpread, float horizontalSpread,
            float speed, int points, double radius) {

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        World w = loc.getWorld();

        double dx, dy, dz;

        double phi, theta;

        for (int i = 0; i < points; i++) {
            phi = 2 * Math.PI * rnd.nextDouble() - Math.PI;
            theta = Math.PI * rnd.nextDouble();

            dx = radius * Math.sin(theta) * Math.cos(phi);
            dy = radius * Math.sin(theta) * Math.sin(phi);
            dz = radius * Math.cos(theta);

            w.spawnParticle(type, x + dx, y + dy, z + dz, amount, horizontalSpread, verticalSpread, horizontalSpread,
                    speed);
        }
    }

}
