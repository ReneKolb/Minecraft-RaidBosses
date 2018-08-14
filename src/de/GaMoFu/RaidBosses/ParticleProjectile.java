package de.GaMoFu.RaidBosses;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.ParticleEffects.LineEffect;
import de.GaMoFu.RaidBosses.ParticleEffects.PointEffect;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.EntityLiving;

public class ParticleProjectile {

	private List<ParticleEffect> onFlyingTick;

	// FIXME: maybe a patterned Effect
	private List<ParticleEffect> onHit;

	private float hitRadius;

	private double maxFlyDistance;

	private double flyedDistance;

	private double damage;
	
	private boolean ignoreArmor;

	private LivingEntity shooter;

	private UUID uuid;

	private Vector dir;

	private World world;

	private Location location;

	private boolean flyThroughNonAir;

	private boolean die;

	public ParticleProjectile(List<ParticleEffect> onFlyingTick, List<ParticleEffect> onHit, float hitRadius,
			double maxFlyDistance, double damage, boolean ignoreArmor, LivingEntity shooter, Vector dir, Location location) {
		this.onFlyingTick = onFlyingTick;
		this.onHit = onHit;
		this.hitRadius = hitRadius;
		this.maxFlyDistance = maxFlyDistance;
		this.damage = damage;
		this.ignoreArmor = ignoreArmor;
		this.shooter = shooter;
		this.dir = dir;
		this.location = location;
		this.world = location.getWorld();

		this.flyThroughNonAir = false;
		this.die = false;

		this.flyedDistance = 0;
		this.uuid = UUID.randomUUID();
	}

	/**
	 * @return the onFlyingTick
	 */
	public List<ParticleEffect> getOnFlyingTick() {
		return onFlyingTick;
	}

	/**
	 * @return the hitRadius
	 */
	public float getHitRadius() {
		return hitRadius;
	}

	/**
	 * @return the maxFlyDistance
	 */
	public double getMaxFlyDistance() {
		return maxFlyDistance;
	}

	/**
	 * @return the flyedDistance
	 */
	public double getFlyedDistance() {
		return flyedDistance;
	}

	/**
	 * @return the damage
	 */
	public double getDamage() {
		return damage;
	}

	/**
	 * @return the shooter
	 */
	public LivingEntity getShooter() {
		return shooter;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the dir
	 */
	public Vector getDir() {
		return dir;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the die
	 */
	public boolean isDie() {
		return die;
	}

	protected Vector getDirectionVector() {
		return this.dir;
	}

	public void tick() {
		double oldX = location.getX();
		double oldY = location.getY();
		double oldZ = location.getZ();

		Vector dirVec = getDirectionVector();

		location.add(dirVec.getX(), dirVec.getY(), dirVec.getZ());

		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();

		this.flyedDistance += dirVec.length();

		Block targetBlock = world.getBlockAt((int) x, (int) y, (int) z);
		Material blockMat = targetBlock.getType();
		if (!this.flyThroughNonAir && blockMat != Material.AIR && blockMat.isBlock()) {
			this.die = true;
			System.out.println("Hit wall");
			// hit a wall
			// trigger explode or so
		}

		for (ParticleEffect e : onFlyingTick) {
			LineEffect.doEffect(world, new Vector(oldX, oldY, oldZ), new Vector(x, y, z), e, 0.3);
		}

		if (this.flyedDistance >= this.maxFlyDistance) {
			this.die = true;
			System.out.println("Flyed too far");
		}

		DamageSource ds;
		if (shooter instanceof Player) {
			ds = DamageSource.playerAttack(((CraftPlayer) shooter).getHandle());
		} else {
			ds = DamageSource.mobAttack(((CraftLivingEntity) shooter).getHandle());
		}
		
		if(this.ignoreArmor) {
			Utils.setIgnoreArmor(ds);
		}

		boolean hit = false;
		for (LivingEntity le : world.getLivingEntities()) {
			if ((shooter instanceof Player) && (le instanceof Player)) {
				continue;
			}
			
			if(le.getUniqueId().equals(shooter.getUniqueId()))
				continue;
			
			if(RaidBosses.getPluginInstance().getHologramHandler().isHologramEntity(le))
				continue;

			if (le.getEyeLocation().distance(this.location) <= this.hitRadius) {
				// hit target
				EntityLiving el = ((CraftLivingEntity) le).getHandle();

				System.out.println("Hit: "+le.getType());
				
				el.damageEntity(ds, (float) this.damage);
				hit = true;
				this.die = true;
			}
		}

		if (hit) {
			for (ParticleEffect e : onHit) {
				PointEffect.doEffect(world, location.toVector(), e);
			}
		}

		// check if hit & trigger damage event
		// DamageSource.projectile(entity, entity1)

	}

}
