package de.GaMoFu.RaidBosses;

import org.bukkit.Location;

public class FirstInstance extends Dungeon {

    public static final String ALIAS = "first";

    @Override
    public Location getPlayerSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void loop() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAlias() {
        return ALIAS;
    }

    @Override
    public String getDisplayName() {
        return "First Dungeon";
    }

}
