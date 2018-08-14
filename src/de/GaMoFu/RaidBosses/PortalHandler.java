package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.GaMoFu.RaidBosses.Events.BossDeathEvent;

public class PortalHandler implements Listener {

    private Map<UUID, List<Portal>> portalMap; // BossID -> spawned Portals

    private RaidBosses plugin;

    public PortalHandler(RaidBosses plugin) {
        this.plugin = plugin;

        this.portalMap = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, loop, 1, 1);
    }

    public void spawnPortal(UUID bossID, Portal portal) {
        List<Portal> portals;
        if (this.portalMap.containsKey(bossID)) {
            portals = this.portalMap.get(bossID);
        } else {
            portals = new LinkedList<>();
            this.portalMap.put(bossID, portals);
        }

        portal.onSpawn();

        portals.add(portal);
    }

    @EventHandler
    public void BossDeath(BossDeathEvent event) {
        UUID bossID = event.getBoss().getEntity().getUniqueId();
        if (!this.portalMap.containsKey(bossID)) {
            return;
        }

        for (Portal p : portalMap.get(bossID)) {
            p.onEnd();
        }

        this.portalMap.remove(bossID);
    }

    private Runnable loop = new Runnable() {

        @Override
        public void run() {

            for (List<Portal> portals : portalMap.values()) {

                List<Portal> toDel = new LinkedList<>();

                for (Portal p : portals) {
                    p.onTick();
                    if (p.isDead()) {
                        toDel.add(p);
                    }
                }

                for (Portal p : toDel) {
                    p.onEnd();
                    portals.remove(p);
                }

            }

        }

    };

}
