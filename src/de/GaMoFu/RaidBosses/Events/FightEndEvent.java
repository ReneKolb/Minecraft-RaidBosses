package de.GaMoFu.RaidBosses.Events;

import org.bukkit.entity.Creature;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.GaMoFu.RaidBosses.Monsters.Monster;

public class FightEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Monster<? extends Creature> monster;
    private boolean reasonIsBossDeath;

    public FightEndEvent(Monster<? extends Creature> monster, boolean reasonIsBossDeath) {
        this.monster = monster;
        this.reasonIsBossDeath = reasonIsBossDeath;
    }

    public Monster<? extends Creature> getMonster() {
        return this.monster;
    }

    public boolean getReasonIsBossDeath() {
        return this.reasonIsBossDeath;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
