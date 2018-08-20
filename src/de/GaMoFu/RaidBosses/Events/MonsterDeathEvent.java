package de.GaMoFu.RaidBosses.Events;

import org.bukkit.entity.Creature;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.GaMoFu.RaidBosses.Monsters.Monster;

public class MonsterDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Monster<? extends Creature> monster;

    public MonsterDeathEvent(Monster<? extends Creature> monster) {
        this.monster = monster;
    }

    /**
     * @return the monster
     */
    public Monster<? extends Creature> getMonster() {
        return monster;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
