package de.GaMoFu.RaidBosses.ParticleEffects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;

public class LineEffect {

	public static void doEffect(World world, Vector start, Vector end, ParticleEffect effect, double pointDistance) {

		double points = end.distance(start) / pointDistance;

		Location currentPos = start.clone().toLocation(world);
		Vector dir = end.subtract(start);
		dir = dir.normalize().multiply(pointDistance);

		for (int i = 0; i < points; i++) {
			currentPos = currentPos.add(dir);
			world.spawnParticle(effect.getType(), currentPos, effect.getCount(), effect.getOffsetX(),
					effect.getOffsetY(), effect.getOffsetZ(), effect.getExtra());
		}
	}

}
