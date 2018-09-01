package de.GaMoFu.RaidBosses.Events;

import org.bukkit.entity.Creature;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.GaMoFu.RaidBosses.Monsters.Monster;

public class FightStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Monster<? extends Creature> monster;

    public FightStartEvent(Monster<? extends Creature> monster) {
        this.monster = monster;
    }

    public Monster<? extends Creature> getMonster() {
        return this.monster;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
