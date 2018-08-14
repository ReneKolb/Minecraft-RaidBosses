package de.GaMoFu.RaidBosses.ParticleEffects;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class CubeEffect {

    private static Random rnd = new Random();

    public static void doEffect(Location loc, Particle type, int amount, float offsetX, float offsetY, float offsetZ,
            double cubeX, double cubeY, double cubeZ, float speed, int points) {

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        World w = loc.getWorld();

        double dx, dy, dz;

        for (int i = 0; i < points; i++) {

            dx = rnd.nextDouble() * 2 * cubeX - cubeX;
            dy = rnd.nextDouble() * 2 * cubeY - cubeY;
            dz = rnd.nextDouble() * 2 * cubeZ - cubeZ;

            w.spawnParticle(type, x + dx, y + dy, z + dz, amount, offsetX, offsetY, offsetZ, speed);
        }
    }

}
