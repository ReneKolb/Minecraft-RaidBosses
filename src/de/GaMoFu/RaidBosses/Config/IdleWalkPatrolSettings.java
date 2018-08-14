package de.GaMoFu.RaidBosses.Config;

import java.util.ArrayList;
import java.util.List;

import de.GaMoFu.RaidBosses.IdleWalk;
import de.GaMoFu.RaidBosses.IdleWalkPatrol;
import de.GaMoFu.RaidBosses.PatrolWaypoint;

public class IdleWalkPatrolSettings extends IdleWalkSettings {

	/**
	 * Specify if the patrol path is cyclic (after the last waypoint begin with the
	 * first one), or if the path should be walked backwards when finished
	 */
	private boolean patrolCircle = false;

	private List<PatrolWaypoint> patrolPoints;

	/**
	 * @return the patrolCircle
	 */
	public boolean isPatrolCircle() {
		return patrolCircle;
	}

	/**
	 * @param patrolCircle
	 *            the patrolCircle to set
	 */
	public void setPatrolCircle(boolean patrolCircle) {
		this.patrolCircle = patrolCircle;
	}

	/**
	 * @return the patrolPoints
	 */
	public List<PatrolWaypoint> getPatrolPoints() {
		if (patrolPoints == null) {
			this.patrolPoints = new ArrayList<>();
		}
		return patrolPoints;
	}

	@Override
	public Class<? extends IdleWalk<? extends IdleWalkSettings>> getIdleWalkClass() {
		return IdleWalkPatrol.class;
	}
	
	

}
