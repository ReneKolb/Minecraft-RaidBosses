package de.GaMoFu.RaidBosses;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;

public class AreaCloudHandler implements Listener {

    private RaidBosses plugin;

    private Map<UUID, AreaCloudSettings> clouds;

    public AreaCloudHandler(RaidBosses plugin) {
        this.plugin = plugin;
        this.clouds = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, loopTask, 1, 1);
    }

    public void handleCustomAreaEffectCloud(AreaEffectCloud cloud, AreaCloudSettings settings) {
        this.clouds.put(cloud.getUniqueId(), settings);
        settings.cloud = cloud;

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                // Cleanup HashMap and trigger onEnd event/skill
                settings.onEnd();
                clouds.remove(cloud.getUniqueId());
            }

        }, cloud.getDuration());

    }

    @EventHandler
    public void onAreaCloudApplyEffect(AreaEffectCloudApplyEvent event) {
        AreaEffectCloud cloud = event.getEntity();

        if (!this.clouds.containsKey(cloud.getUniqueId())) {
            return;
        }

        AreaCloudSettings settings = clouds.get(cloud.getUniqueId());

        // THis event is called every 5 ticks
        settings.updateTimers(5);

        if (settings.isDamageReady()) {
            // ignores armor
            DamageSource damageSource = DamageSource.c((Entity) cloud, (Entity) cloud.getSource());

            for (LivingEntity le : event.getAffectedEntities()) {
                if (plugin.getHologramHandler().isHologramEntity(le))
                    continue;

                ((CraftLivingEntity) le).getHandle().damageEntity(damageSource, (float) settings.getDamage());
            }
            settings.resetDamageTmr();
        }

        if (settings.isSaturationReady()) {
            for (LivingEntity le : event.getAffectedEntities()) {
                if (!(le instanceof Player))
                    continue;
                Player p = (Player) le;
                p.setFoodLevel(Math.min(20, p.getFoodLevel() + settings.getSaturation()));

            }
            settings.resetSaturationTmr();
        }

    }

    private Runnable loopTask = new Runnable() {

        @Override
        public void run() {
            for (AreaCloudSettings settings : clouds.values()) {
                settings.updateTickTimer(1);
                if (settings.getTickTimer() >= settings.cloud.getReapplicationDelay()) {
                    settings.resetTickTimer();
                    settings.onTick();
                }

            }
        }

    };

}
