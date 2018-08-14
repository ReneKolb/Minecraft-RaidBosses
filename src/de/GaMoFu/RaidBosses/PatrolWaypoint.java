package de.GaMoFu.RaidBosses;

import org.bukkit.util.Vector;

public class PatrolWaypoint {

	private double x;

	private double y;

	private double z;

	private int waitDelay;

	public PatrolWaypoint(double x, double y, double z, int waitDelay) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.waitDelay = waitDelay;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	public Vector getVector() {
		return new Vector(x, y, z);
	}

	/**
	 * @return the waitDelay in server ticks (20 = 1sec)
	 */
	public int getWaitDelay() {
		return waitDelay;
	}

}
