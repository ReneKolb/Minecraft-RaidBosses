package de.GaMoFu.RaidBosses.ParticleEffects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class RingEffect {

	public static void doEffect(Location loc, Particle type, int amount, float verticalSpread, float horizontalSpread,
			float speed, float offset, int points, double radius) {
		double startx = loc.getX();
		double starty = loc.getY();
		double startz = loc.getZ();

		World w = loc.getWorld();

		if (points <= 0)
			points = 1;

		float inc = 360F / points;
		amount /= points;

		for (double i = 0D; i < 360D; i += inc) {
			double angle = i * 3.141592653589793D / 180D;
			double x = startx + radius * Math.cos(angle);
			double z = startz + radius * Math.sin(angle);

			w.spawnParticle(type, x, starty + offset, z, amount, horizontalSpread, verticalSpread, horizontalSpread,
					speed);
		}

	}

}
