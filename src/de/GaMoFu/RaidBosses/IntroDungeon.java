package de.GaMoFu.RaidBosses;

import org.bukkit.Location;

import de.GaMoFu.RaidBosses.Dungeons.Dungeon;

public class IntroDungeon extends Dungeon {

    public static final String ALIAS = "intro";

    @Override
    public Location getPlayerSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAlias() {
        return ALIAS;
    }

    @Override
    public String getDisplayName() {
        return "Intro Dungeon";
    }

    @Override
    public void loop() {
        // TODO Auto-generated method stub

    }

}
