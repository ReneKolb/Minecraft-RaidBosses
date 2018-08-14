package de.GaMoFu.RaidBosses;

import org.bukkit.Particle;

public class ParticleEffect {

    private Particle type;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double extra;

    public ParticleEffect(Particle type, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.type = type;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
    }

    /**
     * @return the type
     */
    public Particle getType() {
        return type;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the offsetX
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * @return the offsetY
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     * @return the offsetZ
     */
    public double getOffsetZ() {
        return offsetZ;
    }

    /**
     * @return the extra
     */
    public double getExtra() {
        return extra;
    }

}
