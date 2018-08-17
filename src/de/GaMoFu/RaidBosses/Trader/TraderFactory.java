package de.GaMoFu.RaidBosses.Trader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.GaMoFu.RaidBosses.RaidBosses;

public class TraderFactory {

    private RaidBosses plugin;

    /** For looking up a trader by its internal name */
    private Map<String, Trader> traderNameLookup;

    private List<String> internalTraderNames;

    public TraderFactory(RaidBosses plugin) {
        this.plugin = plugin;
        this.traderNameLookup = new HashMap<>();

        init();
    }

    public List<String> getTraderInternalNames() {
        if (internalTraderNames == null) {
            internalTraderNames = new LinkedList<>(traderNameLookup.keySet());
        }
        return internalTraderNames;
    }

    // Load locations and typed frpm Config

    private void init() {
        this.plugin.getLogger().info("Initializing traders");

        for (TradersEnum t : TradersEnum.values()) {
            Class<? extends Trader> traderClass = t.getTraderClass();
            Trader trader;
            try {
                trader = traderClass.newInstance();

                this.traderNameLookup.put(trader.getInternalName(), trader);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Trader getTrader(String internalName) {
        return this.traderNameLookup.get(internalName);
    }
}
