package de.GaMoFu.RaidBosses.Worlds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Items.Item;
import net.md_5.bungee.api.ChatColor;

public class Worlds implements Listener {

    private static final String CONFIG_WORLDS_LIST = "worlds_list";

    private RaidBosses plugin;

    private List<String> worlds;

    private boolean configChanged;

    public void applyGameRules(World world) {
        plugin.getLogger().info("Applying World Rules to world " + world.getName());

        world.setGameRule(GameRule.DO_FIRE_TICK, false); // disable fire spread and extinguish
        world.setGameRule(GameRule.DO_MOB_LOOT, false); // disable mob naturally drop items
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false); // disable weather cycle
        world.setGameRule(GameRule.KEEP_INVENTORY, true); // player do not drop Inventory on death
        world.setGameRule(GameRule.MOB_GRIEFING, false); // disable mobs can change blocks / creeper destroy blocks
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0); // disable natural growing plants etc.
    }

    @SuppressWarnings("unchecked")
    public Worlds(RaidBosses plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.configChanged = false;

        Object worldsList = plugin.getConfig().get(CONFIG_WORLDS_LIST);
        if (worldsList instanceof List) {
            worlds = (List<String>) worldsList;
        } else {
            worlds = new ArrayList<>();
        }

        plugin.getLogger().info("Loading worlds...");
        for (String world : worlds) {
            if (plugin.getServer().getWorld(world) != null) {
                plugin.getLogger().info("\tWorld " + world + " already loaded.");
                continue;
            }

            plugin.getLogger().info("\tLoad world " + world);
            WorldCreator wc = new WorldCreator(world);
            wc.generator(new RaidInstanceWorldGenerator()); // override the world creator to not automatically create
                                                            // new chunks
            World w = plugin.getServer().createWorld(wc);

            // kill all creatures
            for (Creature c : w.getEntitiesByClass(Creature.class)) {
                c.remove();
            }

            // applying gamerules
            applyGameRules(w);

        }
        plugin.getLogger().info("All worlds loaded");
    }

    public void saveWorldsList() {
        if (!this.configChanged) {
            plugin.getLogger().info("Skip saving worlds list. No changes.");
            return;
        }
        plugin.getLogger().info("Saving worlds list");
        plugin.getConfig().set(CONFIG_WORLDS_LIST, worlds);
        plugin.getLogger().info("Worlds list saved");
    }

    public boolean createWorld(String name) {
        if (worldExists(name)) {
            return false;
        }

        WorldCreator raidInstanceCreator = new WorldCreator(name);

        raidInstanceCreator.environment(Environment.NORMAL);
        raidInstanceCreator.generateStructures(false);
        raidInstanceCreator.generator(new RaidInstanceWorldGenerator());
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "Creating world " + name);
        World world = plugin.getServer().createWorld(raidInstanceCreator);

        // temporary set the spawn location to 0,1,0 since this is the only available
        // block in the world due to RaidInstanceWorldGenerator
        world.setSpawnLocation(0, 2, 0);

        applyGameRules(world);

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "done");

        worlds.add(name);

        this.configChanged = true;

        return true;
    }

    public boolean worldExists(String name) {
        return worlds.contains(name);
    }

    public World getWorld(String name) {
        if (!worldExists(name)) {
            plugin.getLogger().info("Cannot get world " + name + ". It is not registered and therefore not loaded.");
            return null;
        }

        return plugin.getServer().getWorld(name);
    }

    public List<String> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // prevent any monster from natural spawning in an instance world.
        // only custom spawns are allowed.

        if (event.getSpawnReason() == SpawnReason.SLIME_SPLIT) {
            plugin.getLogger().info("Spawning splitted slimes is cancelled in instance worlds.");
        }

        if (event.getSpawnReason() != SpawnReason.NATURAL) {
            System.out.println("Spawn entity: " + event.getEntityType() + " Reason: " + event.getSpawnReason());
        }

        if (event.getEntityType() == EntityType.ARMOR_STAND) {
            // Let an armor_stand spawn
            return;
        }

        // Allow spawner eggs
        if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
            return;

        String worldName = event.getLocation().getWorld().getName();
        if (worldExists(worldName) && event.getSpawnReason() != SpawnReason.CUSTOM) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        // If a TNT that is primed by a player breaks a hanging, the Remover is the
        // player (not the PrimedTNT). so generally cancel explosion causes rather than
        // player!=creative
        if (event.getCause() == RemoveCause.EXPLOSION) {
            event.setCancelled(true);
            return;
        }

        if (event.getRemover() instanceof Player) {
            if (((Player) event.getRemover()).getGameMode() == GameMode.CREATIVE) {
                return;
            }
        }
        // prevent players and explosions to break paintings and item frames
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    // @EventHandler
    // public void onBlockFade(BlockFadeEvent event) {
    // System.out.println("Fade: "+event.getBlock().getType());
    // System.out.println("New: "+event.getNewState().getType());
    //
    // }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        Player player = getSourceDamager(event.getAttacker());

        if (player == null) {
            event.setCancelled(true);
            return;
        }

        // Only Creative Players are allowed to break MineCarts / Vehicles
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        Player damagerPlayer = getSourceDamager(event.getDamager());
        // Creative Players are allowed to break entities

        if (event.getEntity() instanceof Villager) {
            // TODO use a merchant Class, to check if this villager is an actual Trader or a
            // spawned monster. But for now assume Villager = Trader
            event.setCancelled(true);
            return;
        }

        if (damagerPlayer != null) {
            if (damagerPlayer.getGameMode() != GameMode.CREATIVE) {
                ItemStack weapon = damagerPlayer.getInventory().getItemInMainHand();
                if (weapon != null && weapon.getItemMeta() != null) {
                    Optional<Item> weaponItem = plugin.getItemsFactory()
                            .getItemFromDisplayName(weapon.getItemMeta().getDisplayName());
                    if (weaponItem.isPresent()) {
                        weaponItem.get().getItemEffects().forEach(effect -> effect.onDamageEntity(event));
                    }
                }
                plugin.getHologramHandler().displayDamage(damagerPlayer, event.getEntity(), event.getFinalDamage(),
                        event.getCause());
            }
        }

        // Prevent decorative entities to break
        // TODO: Seems not to work for minecarts
        switch (event.getEntityType()) {
        case ARMOR_STAND:
        case DROPPED_ITEM:
        case MINECART:
        case MINECART_CHEST:
        case MINECART_COMMAND:
        case MINECART_FURNACE:
        case MINECART_HOPPER:
        case MINECART_MOB_SPAWNER:
        case MINECART_TNT:
            event.setCancelled(true);
            break;
        default:
        }

    }

    private Player getSourceDamager(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }

        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }

        return null;
    }

}
