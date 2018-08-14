package de.GaMoFu.RaidBosses.Config;

import de.GaMoFu.RaidBosses.IdleWalk;
import de.GaMoFu.RaidBosses.IdleWalkStroll;

public class IdleWalkStrollSettings extends IdleWalkSettings {

    private float maxStrollRadius;

    private int minStrollDelay;

    private int maxStrollDelay;

    /**
     * @return the maxStrollRadius
     */
    public float getMaxStrollRadius() {
        return maxStrollRadius;
    }

    /**
     * @param maxStrollRadius
     *            the maxStrollRadius to set
     */
    public void setMaxStrollRadius(float maxStrollRadius) {
        this.maxStrollRadius = maxStrollRadius;
    }

    /**
     * @return the minStrollDelay
     */
    public int getMinStrollDelay() {
        return minStrollDelay;
    }

    /**
     * @param minStrollDelay
     *            the minStrollDelay to set
     */
    public void setMinStrollDelay(int minStrollDelay) {
        this.minStrollDelay = minStrollDelay;
        this.maxStrollDelay = Math.max(this.minStrollDelay, this.maxStrollDelay);
    }

    /**
     * @return the maxStrollDelay
     */
    public int getMaxStrollDelay() {
        return maxStrollDelay;
    }

    /**
     * @param maxStrollDelay
     *            the maxStrollDelay to set
     */
    public void setMaxStrollDelay(int maxStrollDelay) {
        this.maxStrollDelay = maxStrollDelay;
        this.minStrollDelay = Math.min(this.maxStrollDelay, this.minStrollDelay);
    }

    @Override
    public Class<? extends IdleWalk<? extends IdleWalkSettings>> getIdleWalkClass() {
        return IdleWalkStroll.class;
    }

}
