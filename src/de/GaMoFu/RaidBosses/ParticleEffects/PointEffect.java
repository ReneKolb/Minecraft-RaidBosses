package de.GaMoFu.RaidBosses.ParticleEffects;

import org.bukkit.World;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffect;

public class PointEffect {

    public static void doEffect(World world, Vector point, ParticleEffect effect) {
        world.spawnParticle(effect.getType(), point.toLocation(world), effect.getCount(), effect.getOffsetX(),
                effect.getOffsetY(), effect.getOffsetZ(), effect.getExtra());
    }

}
