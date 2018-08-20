package de.GaMoFu.RaidBosses.Monsters;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import de.GaMoFu.RaidBosses.RaidBosses;

public abstract class Boss<T extends Creature> extends Monster<T> {

    protected BossBar bossBar;

    public Boss(Class<T> type) {
        super(type);
        this.bossBar = createBossBar();
    }

    protected abstract BossBar createBossBar();

    // public abstract List<ItemStack> getLoot();

    // public abstract int getDropLootAmount();

    // public abstract String getTokenName();

    // public abstract int getTokenAmount();

    protected void updateHealthBar() {
        if (this.entity == null)
            return;

        double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double health = entity.getHealth();

        bossBar.setProgress(health / maxHealth);
    }

    @Override
    protected boolean showHealthAsName() {
        // since a boss has its own BIG Boss bar
        return false;
    }

    @Override
    public void onFindTarget(Entity newTarget) {
        updateHealthBar();
        for (Player p : this.entity.getWorld().getPlayers()) {
            bossBar.addPlayer(p);
        }

    }

    @Override
    public void onDeath(EntityDeathEvent event) {
        bossBar.setProgress(0);
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        RaidBosses.getPluginInstance().getServer().getScheduler()
                .scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                    @Override
                    public void run() {
                        updateHealthBar();
                    }

                }, 1);
    }

    @Override
    public void onFightStart() {
        updateHealthBar();
        for (Player p : this.entity.getWorld().getPlayers()) {
            bossBar.addPlayer(p);
        }
    }

    @Override
    public void onFightEnd(boolean reasonIsBossDeath) {
        // Hide the health bar after 1.5sec
        Bukkit.getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

            @Override
            public void run() {
                bossBar.removeAll();
            }

        }, 30);

    }

}
