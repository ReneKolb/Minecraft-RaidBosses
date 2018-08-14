package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Aggro {

    private Map<UUID, Integer> aggroList;

    private Random rnd = new Random();

    public Aggro() {
        this.aggroList = new HashMap<>();
    }

    public void reset() {
        this.aggroList.clear();
    }

    private Entry<UUID, Integer> findHighestAggroEntry() {
        if (aggroList.isEmpty()) {
            return null;
        }

        Entry<UUID, Integer> highestEntry = null;
        int highest = Integer.MIN_VALUE;

        for (Map.Entry<UUID, Integer> entry : aggroList.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                highestEntry = entry;
            }
        }

        return highestEntry;
    }

    public UUID getRandomTarget() {
        Set<UUID> ids = aggroList.keySet();
        int index = rnd.nextInt(ids.size());
        int i = 0;

        for (UUID id : ids) {
            if (i == index) {
                return id;
            }
            i++;
        }

        // should never happen, only if no entry in aggroList
        return null;
    }

    /**
     * Same as {@link addAggro(UUID, int)} but round amount to ceil.
     */
    public UUID addAggro(UUID damagerID, double amount) {
        return this.addAggro(damagerID, (int) Math.ceil(amount));
    }

    /**
     * 
     * @param damagerID
     *            The damager UUID
     * @param amount
     *            The amount of aggro to add or subtract (if negative)
     * 
     * @return The UUID of the player/entity with the highest aggro (after
     *         modifying).
     */
    public UUID addAggro(UUID damagerID, int amount) {
        // The total aggro amount per player cannot be negative. So since amount may be
        // negative,
        // the added value must be checked and corrected (>=0) if needed.

        int newAggro;
        if (aggroList.containsKey(damagerID)) {
            newAggro = Math.max(0, aggroList.get(damagerID) + amount);
        } else {
            newAggro = Math.max(0, amount);
        }

        aggroList.put(damagerID, newAggro);

        Entry<UUID, Integer> highestAggro = findHighestAggroEntry();

        if (highestAggro == null)
            return null;

        return highestAggro.getKey();
    }

    /**
     * Remove the aggro entry from the given Damager. This is used when the damager
     * dies or logs off.
     * 
     * @param damagerID
     *            The UUID to remove
     * 
     * @return The new UUID with the (now) highest aggro amount.
     */
    public UUID removeDamager(UUID damagerID) {
        this.aggroList.remove(damagerID);

        Entry<UUID, Integer> highestAggro = findHighestAggroEntry();

        if (highestAggro == null)
            return null;

        return highestAggro.getKey();
    }

}
