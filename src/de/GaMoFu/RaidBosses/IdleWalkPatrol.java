package de.GaMoFu.RaidBosses;

import de.GaMoFu.RaidBosses.Config.IdleWalkPatrolSettings;

public class IdleWalkPatrol extends IdleWalk<IdleWalkPatrolSettings> {

	public static final double DISTANCE_TO_WAYPOINT_TOLERANCE = 1.0; // the walking / navigation of entities is not that
																		// accurate

	private int targetPatrolPointCounter;
	private int checkDistanceTmr; // every time this timer is 0, check the distance to target waypoint
	private boolean waitingAtTarget;

	private boolean started;

	public IdleWalkPatrol() {
		super();

		this.targetPatrolPointCounter = 0;
		this.checkDistanceTmr = 20;
		this.waitingAtTarget = false;
		this.started = false;
	}

	private void incPatrolCounter() {
		int size = this.idleWalkSettings.getPatrolPoints().size();

		this.targetPatrolPointCounter++;

		if (this.idleWalkSettings.isPatrolCircle()) {
			if (this.targetPatrolPointCounter >= size) {
				this.targetPatrolPointCounter = 0;
			}
		} else {
			if (this.targetPatrolPointCounter >= 2 * size - 1) {
				this.targetPatrolPointCounter = 0;
			}
		}
	}

	private int getPatrolIndex() {
		if (this.idleWalkSettings.isPatrolCircle()) {
			return this.targetPatrolPointCounter;
		} else {
			int size = this.idleWalkSettings.getPatrolPoints().size();
			if (this.targetPatrolPointCounter < size) {
				return this.targetPatrolPointCounter;
			} else {
				return 2 * (size - 1) - this.targetPatrolPointCounter;
			}
		}

	}

	protected void walkToWaypoint(PatrolWaypoint waypoint) {
		double x = waypoint.getX();
		double y = waypoint.getY();
		double z = waypoint.getZ();
		double speed = 1d;// getCraftCreature().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
		// System.out.println("Walk to (" + x + "," + y + "," + z + ") with speed: " +
		// speed);
		getCraftCreature().getHandle().getNavigation().a(x, y, z, speed);
	}

	@Override
	public void loop() {

		this.checkDistanceTmr--;
		if (this.checkDistanceTmr <= 0) {
			this.checkDistanceTmr = 5;

			if (!this.started) {
				PatrolWaypoint nextWaypoint = this.idleWalkSettings.getPatrolPoints().get(getPatrolIndex());
				walkToWaypoint(nextWaypoint);

				this.started = true;
				return;
			}

			PatrolWaypoint targetWaypoint = this.idleWalkSettings.getPatrolPoints().get(getPatrolIndex());
			double distToNextWaypoint = getCraftCreature().getLocation()
					.distance(targetWaypoint.getVector().toLocation(this.getCraftCreature().getWorld()));
			if (distToNextWaypoint <= DISTANCE_TO_WAYPOINT_TOLERANCE) {
				if (!this.waitingAtTarget && targetWaypoint.getWaitDelay() > 0) {
					// Let the monster way at the target location the given period of time
					this.checkDistanceTmr = targetWaypoint.getWaitDelay();
					this.waitingAtTarget = true;
				} else {
					incPatrolCounter();
					this.waitingAtTarget = false;
					PatrolWaypoint nextWaypoint = this.idleWalkSettings.getPatrolPoints().get(getPatrolIndex());
					walkToWaypoint(nextWaypoint);
				}
			} else if (!isWalking()) {
				// sometimes the monster stops walking before reaching the actual waypoint.
				// In this case the monster has to be triggered again to walk towards the target
				// waypoint

				PatrolWaypoint nextWaypoint = this.idleWalkSettings.getPatrolPoints().get(getPatrolIndex());
				walkToWaypoint(nextWaypoint);
			}
		}
	}

}
