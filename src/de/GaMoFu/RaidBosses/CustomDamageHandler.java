package de.GaMoFu.RaidBosses;

import org.bukkit.event.Listener;

public class CustomDamageHandler implements Listener {

    // public class DamageSettings {
    // double damage;
    // }
    //
    // private Map<UUID, DamageSettings> internalMap;
    //
    // private RaidBosses plugin;
    //
    // public CustomDamageHandler(RaidBosses plugin) {
    // this.plugin = plugin;
    //
    // this.internalMap = new HashMap<>();
    //
    // this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    // }
    //
    // public void setCustomDamageToEntity(final UUID entityID, double damage) {
    // DamageSettings ds = new DamageSettings();
    // ds.damage = damage;
    // this.internalMap.put(entityID, ds);
    //
    // plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new
    // Runnable() {
    //
    // @Override
    // public void run() {
    // internalMap.remove(entityID);
    // }
    // }, 20 * 30); // remove after 30sec
    // }
    //
    // @EventHandler(priority = EventPriority.LOWEST) // call this event the first
    // public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    // if (!internalMap.containsKey(event.getDamager().getUniqueId()))
    // return;
    //
    // DamageSettings sett = internalMap.get(event.getDamager().getUniqueId());
    // event.setDamage(sett.damage);
    // }

}
