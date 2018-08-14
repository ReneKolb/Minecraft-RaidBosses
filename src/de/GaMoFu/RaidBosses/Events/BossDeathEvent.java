package de.GaMoFu.RaidBosses.Events;

import org.bukkit.entity.Creature;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.GaMoFu.RaidBosses.Monsters.Boss;

public class BossDeathEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Boss<? extends Creature> boss;

	public BossDeathEvent(Boss<? extends Creature> boss) {
		this.boss = boss;
	}

	public Boss<? extends Creature> getBoss() {
		return this.boss;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
