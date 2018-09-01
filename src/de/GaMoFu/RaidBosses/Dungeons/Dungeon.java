package de.GaMoFu.RaidBosses.Dungeons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.google.gson.GsonBuilder;

import de.GaMoFu.RaidBosses.IdleWalk;
import de.GaMoFu.RaidBosses.IdleWalkPatrol;
import de.GaMoFu.RaidBosses.IdleWalkStroll;
import de.GaMoFu.RaidBosses.PatrolWaypoint;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedBoss;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Utils;
import de.GaMoFu.RaidBosses.Config.IdleWalkPatrolSettings;
import de.GaMoFu.RaidBosses.Config.IdleWalkSettings;
import de.GaMoFu.RaidBosses.Config.IdleWalkSettingsClassAdapter;
import de.GaMoFu.RaidBosses.Config.IdleWalkStrollSettings;
import de.GaMoFu.RaidBosses.Config.MonsterConfig;
import de.GaMoFu.RaidBosses.Config.SaveConfig;
import de.GaMoFu.RaidBosses.Events.BossDeathEvent;
import de.GaMoFu.RaidBosses.Events.MonsterDeathEvent;
import de.GaMoFu.RaidBosses.Monsters.Boss;
import de.GaMoFu.RaidBosses.Monsters.BossType;
import de.GaMoFu.RaidBosses.Monsters.Monster;
import de.GaMoFu.RaidBosses.Monsters.MonsterType;
import net.md_5.bungee.api.ChatColor;

public abstract class Dungeon implements Listener {

    protected RaidBosses plugin;

    protected World world;

    protected File configFile;

    public Location getPlayerSpawnLocation() {
        return this.world.getSpawnLocation();
    }

    protected BossBar bossBar;

    protected Map<Long, MonsterConfig> monstersConfiguration;
    protected Map<UUID, SpawnedMonster> monsters; // spawnLoc, entity, specials

    protected Map<UUID, SpawnedMonster> customSpawnedMonsters;

    protected Map<Long, MonsterConfig> bossesConfiguration;
    protected Map<UUID, SpawnedBoss> bosses;

    protected List<UUID> players;

    private boolean configChanged;

    protected Scoreboard scoreboard;
    protected Objective healthDisplayHead;
    protected Objective healthDisplaySide;
    protected Team dungeonTeam;
    protected ScoreboardSide healthDisplaySideHandler;

    public void init(RaidBosses plugin, World world) {
        this.plugin = plugin;
        this.world = world;

        this.monsters = new HashMap<>();
        this.bosses = new HashMap<>();
        this.players = new ArrayList<>();

        this.customSpawnedMonsters = new HashMap<>();

        loadConfig();

        spawnAllMonsters();

        spawnAllBosses();

        configChanged = false;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // only 16 characters are allowed for names
        this.healthDisplayHead = scoreboard.registerNewObjective("HealthDispHead", "dummy", "Health");
        healthDisplayHead.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthDisplayHead.setDisplayName(org.bukkit.ChatColor.LIGHT_PURPLE + "% Health");

        this.healthDisplaySide = scoreboard.registerNewObjective("HealthDispSide", "dummy",
                ChatColor.YELLOW + this.getDisplayName());
        healthDisplaySide.setDisplaySlot(DisplaySlot.SIDEBAR);
        // healthDisplaySide.setDisplayName(ChatColor.YELLOW + this.getDisplayName());

        this.dungeonTeam = scoreboard.registerNewTeam("DungeonTeam");
        dungeonTeam.setAllowFriendlyFire(false);

        this.healthDisplaySideHandler = new ScoreboardSide(plugin, this.scoreboard, this.healthDisplaySide, 7);

        // plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                for (SpawnedMonster sm : monsters.values()) {
                    sm.loop();
                }

                for (SpawnedMonster csm : customSpawnedMonsters.values()) {
                    csm.loop();
                }

                for (SpawnedMonster sb : bosses.values()) {
                    sb.loop();
                }

                loop();
            }

        }, 1, 1);
    }

    public abstract String getAlias();

    public abstract String getDisplayName();

    public World getWorld() {
        return this.world;
    }

    protected void loadConfig() {
        this.configFile = new File(plugin.getDataFolder(), world.getName() + ".yml");
        try {
            // Create the file if it does not exist
            boolean created = this.configFile.createNewFile();
            if (created) {
                plugin.getLogger().info("No configuration file for '" + this.getDisplayName() + "' found. Created.");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        FileReader reader;
        try {
            reader = new FileReader(this.configFile);

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(IdleWalkSettings.class, new IdleWalkSettingsClassAdapter());
            SaveConfig savedConfig = gson.create().fromJson(reader, SaveConfig.class);

            Collection<MonsterConfig> monsters;
            Collection<MonsterConfig> bosses;

            if (savedConfig == null) {
                monsters = new ArrayList<>();
                bosses = new ArrayList<>();
            } else {
                monsters = savedConfig.getMonsterConfig();
                bosses = savedConfig.getBossesConfig();
            }

            if (monsters == null || monsters.isEmpty()) {
                this.monstersConfiguration = new HashMap<>();
            } else {
                this.monstersConfiguration = monsters.stream()
                        .collect(Collectors.toMap(MonsterConfig::getConfigID, Function.identity()));
            }

            if (bosses == null || bosses.isEmpty()) {
                this.bossesConfiguration = new HashMap<>();
            } else {
                this.bossesConfiguration = bosses.stream()
                        .collect(Collectors.toMap(MonsterConfig::getConfigID, Function.identity()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // this.config = YamlConfiguration.loadConfiguration(configFile);

        // JSONArr
        // ObjectMapper mapper = new ObjectMapper();
        // mapper.enableDefaultTyping();
        // List<Collection<MonsterConfig>> loadedConfig = null;
        // try {
        // loadedConfig = mapper.readValue(this.configFile, new
        // TypeReference<List<Collection<MonsterConfig>>>() {
        // });
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // Collection<MonsterConfig> monsterConf = loadedConfig.get(0);
        // Collection<MonsterConfig> bossConf = loadedConfig.get(1);

        // Collection<MonsterConfig> monsterConf = new ArrayList<>();
        // MonsterConfig mco = new MonsterConfig();
        // mco.setConfigID(0);
        // mco.setMonsterType("zombie");
        // mco.setX(0);
        // mco.setY(15);
        // mco.setZ(0);
        //
        // IdleWalkStrollSettings walk = new IdleWalkStrollSettings();
        // walk.setMinStrollDelay(20 * 3);
        // walk.setMaxStrollDelay(20 * 6);
        // walk.setMaxStrollRadius(5);
        //
        // mco.setIdleWalkSettings(walk);
        //
        // monsterConf.add(mco);
        //
        // this.monstersConfiguration = new HashMap<>();
        // this.monstersConfiguration.put(mco.getConfigID(), mco);
        // this.bossesConfiguration = new HashMap<>();
        //
        // Collection<MonsterConfig> bossConf = new ArrayList<>();
        //
        // if (monsterConf == null || monsterConf.isEmpty()) {
        // this.monstersConfiguration = new HashMap<>();
        // } else {
        // this.monstersConfiguration = monsterConf.stream()
        // .collect(Collectors.toMap(mc -> mc.getConfigID(), mc -> mc));
        // }
        //
        // if (bossConf == null || bossConf.isEmpty()) {
        // this.bossesConfiguration = new HashMap<>();
        // } else {
        // this.bossesConfiguration = bossConf.stream().collect(Collectors.toMap(bc ->
        // bc.getConfigID(), bc -> bc));
        // }

        // List<Collection<MonsterConfig>> saveList =
        // Arrays.asList(this.monstersConfiguration.values(),
        // this.bossesConfiguration.values());
        //
        // mapper.writeValue(this.configFile, saveList);
        //
        // List<String> monstersConfig = config.getStringList(CONFIG_MONSTERS_KEY);
        // if (monstersConfig == null) {
        // this.monstersConfiguration = new HashMap<>();
        // } else {
        //
        // this.monstersConfiguration = monstersConfig.stream().map(mc ->
        // MonsterConfig.fromSaveString(mc))
        // .collect(Collectors.toMap(mc -> mc.getConfigID(), mc -> mc));
        // }
        //
        // List<String> bossesConfig = config.getStringList(CONFIG_BOSSES_KEY);
        // if (bossesConfig == null) {
        // this.bossesConfiguration = new HashMap<>();
        // } else {
        // this.bossesConfiguration = bossesConfig.stream().map(bc ->
        // MonsterConfig.fromSaveString(bc))
        // .collect(Collectors.toMap(bc -> bc.getConfigID(), bc -> bc));
        // }

    }

    private long findFreeConfigID(Set<Long> existingIDs) {
        long id = 0;

        while (existingIDs.contains(id))
            id++;

        return id;
    }

    public SpawnedMonster addCustomSpawnedMonster(MonsterType monsterType, Location loc) {
        try {

            Monster<? extends Creature> monster = monsterType.getEntityClass().newInstance();

            monster.spawn(loc);

            SpawnedMonster spawnedMonster = new SpawnedMonster(this, -1);
            spawnedMonster.setMonsterEntity(monster);
            spawnedMonster.setSpawnLocation(loc);

            this.customSpawnedMonsters.put(monster.getEntity().getUniqueId(), spawnedMonster);

            return spawnedMonster;

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void removeCustomSpawnedMonster(UUID id) {
        this.customSpawnedMonsters.remove(id);
    }

    public boolean addMonster(MonsterType monsterType, Location loc) {
        plugin.getLogger().info("Adding Monster " + monsterType.getAliasName() + " at " + Utils.LocationToString(loc));
        try {
            MonsterConfig monsterConfig = new MonsterConfig();
            long configID = findFreeConfigID(this.monstersConfiguration.keySet());
            monsterConfig.setConfigID(configID);
            monsterConfig.setMonsterType(monsterType.getAliasName());
            monsterConfig.setX(loc.getX());
            monsterConfig.setY(loc.getY());
            monsterConfig.setZ(loc.getZ());

            this.monstersConfiguration.put(configID, monsterConfig);

            Monster<? extends Creature> monster = monsterType.getEntityClass().newInstance();

            monster.spawn(loc);

            SpawnedMonster spawnedMonster = new SpawnedMonster(this, configID);
            spawnedMonster.setMonsterEntity(monster);
            spawnedMonster.setSpawnLocation(loc);

            this.monsters.put(monster.getEntity().getUniqueId(), spawnedMonster);

            this.configChanged = true;

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addMonster(String alias, Location loc) {
        plugin.getLogger().info("Adding monster '" + alias + "' to instance '" + this.getAlias() + "'");
        Optional<MonsterType> type = MonsterType.fromAliasName(alias);
        if (!type.isPresent()) {
            plugin.getLogger().info("Cannot find MonsterType '" + alias + "'");
            return false;
        }

        return addMonster(type.get(), loc);
    }

    public boolean addBoss(BossType bossType, Location loc) {
        try {
            MonsterConfig bossesConfig = new MonsterConfig();
            long configID = findFreeConfigID(this.bossesConfiguration.keySet());
            bossesConfig.setConfigID(configID);
            bossesConfig.setMonsterType(bossType.getAliasName());
            bossesConfig.setX(loc.getX());
            bossesConfig.setY(loc.getY());
            bossesConfig.setZ(loc.getZ());

            this.bossesConfiguration.put(configID, bossesConfig);

            Boss<? extends Creature> monster = (Boss<? extends Creature>) bossType.getEntityClass().newInstance();

            monster.spawn(loc);

            SpawnedBoss spawnedBoss = new SpawnedBoss(this, configID);
            spawnedBoss.setMonsterEntity(monster);
            spawnedBoss.setSpawnLocation(loc);

            this.bosses.put(monster.getEntity().getUniqueId(), spawnedBoss);

            this.configChanged = true;

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addBoss(String alias, Location loc) {
        plugin.getLogger().info("Adding boss '" + alias + "' to instance '" + this.getAlias() + "'");
        Optional<BossType> type = BossType.fromAliasName(alias);
        if (!type.isPresent()) {
            plugin.getLogger().info("Cannot find BossType '" + alias + "'");
            return false;
        }

        return addBoss(type.get(), loc);
    }

    public Optional<SpawnedBoss> getSpawnedBoss(UUID entityID) {
        if (!this.bosses.containsKey(entityID)) {
            return Optional.empty();
        }

        return Optional.of(this.bosses.get(entityID));
    }

    public void saveConfig() {
        if (!this.configChanged) {
            plugin.getLogger().info("Skip saving Monster spawns. No changes.");
            return;
        }

        plugin.getLogger().info("Saving monster spawns for instance " + this.getAlias());

        SaveConfig saveConfig = new SaveConfig(this.monstersConfiguration.values(), this.bossesConfiguration.values());

        FileWriter writer;
        try {
            writer = new FileWriter(this.configFile);

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(IdleWalkSettings.class, new IdleWalkSettingsClassAdapter());
            gson.create().toJson(saveConfig, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try {
        // JSONArray<> jsonArray = new JSONArray();

        // ObjectMapper mapper = new ObjectMapper();
        // mapper.readValue(this.configFile, new
        // TypeReference<List<Collection<MonsterConfig>>>() {});
        // List<Collection<MonsterConfig>> saveList =
        // Arrays.asList(this.monstersConfiguration.values(),
        // this.bossesConfiguration.values());
        // jsonArray.addAll(saveList);
        //
        // mapper.writeValue(this.configFile, saveList);

        // config.set(CONFIG_MONSTERS_KEY,
        // this.monstersConfiguration.values().stream().map(mc -> mc.toSaveString())
        // .collect(Collectors.toCollection(ArrayList::new)));
        // config.set(CONFIG_BOSSES_KEY,
        // this.bossesConfiguration.values().stream().map(mc -> mc.toSaveString())
        // .collect(Collectors.toCollection(ArrayList::new)));

        // this.config.save(this.configFile);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

    /**
     * This method is called every server tick. Implement any instance logic here
     * that needs a cyclic update.
     */
    public abstract void loop();

    @SuppressWarnings("unchecked")
    protected void spawnAllMonsters() {
        plugin.getLogger().info("Spawning all monsters");
        for (Map.Entry<Long, MonsterConfig> monsterConfig : monstersConfiguration.entrySet()) {
            Optional<MonsterType> type = MonsterType.fromAliasName(monsterConfig.getValue().getMonsterTypeAlias());

            if (!type.isPresent()) {
                plugin.getLogger().warning("Cannot spawn configured monster. Type '"
                        + monsterConfig.getValue().getMonsterTypeAlias() + "' not found.");
                continue;
            }

            SpawnedMonster spawnedMonster = new SpawnedMonster(this, monsterConfig.getKey());

            Location spawnLocation = new Location(this.world, monsterConfig.getValue().getX(),
                    monsterConfig.getValue().getY(), monsterConfig.getValue().getZ());
            spawnedMonster.setSpawnLocation(spawnLocation);

            try {
                Monster<? extends Creature> monster = type.get().getEntityClass().newInstance();

                monster.spawn(spawnLocation);
                spawnedMonster.setMonsterEntity(monster);

                IdleWalkSettings idleWalkSettings = monsterConfig.getValue().getIdleWalkSettings();
                if (idleWalkSettings != null) {
                    @SuppressWarnings("rawtypes")
                    IdleWalk idleWalk = idleWalkSettings.getIdleWalkClass().newInstance();
                    idleWalk.setIdleWalkSettings(idleWalkSettings);
                    idleWalk.setMonster(monster);

                    monster.setIdleWalk(idleWalk);
                    // TODO also do for bosses
                }

                this.monsters.put(monster.getEntity().getUniqueId(), spawnedMonster);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected void spawnAllBosses() {
        plugin.getLogger().info("Spawning all bosses");
        for (Map.Entry<Long, MonsterConfig> bossConfig : bossesConfiguration.entrySet()) {
            Optional<BossType> type = BossType.fromAliasName(bossConfig.getValue().getMonsterTypeAlias());

            if (!type.isPresent()) {
                plugin.getLogger().warning("Cannot spawn configured boss. Type '"
                        + bossConfig.getValue().getMonsterTypeAlias() + "' not found.");
                continue;
            }

            SpawnedBoss spawnedBoss = new SpawnedBoss(this, bossConfig.getKey());

            Location spawnLocation = new Location(this.world, bossConfig.getValue().getX(),
                    bossConfig.getValue().getY(), bossConfig.getValue().getZ());
            spawnedBoss.setSpawnLocation(spawnLocation);

            if (bossConfig.getValue().getLootChestX() != null && bossConfig.getValue().getLootChestY() != null
                    && bossConfig.getValue().getLootChestZ() != null) {
                Location lootChestLocation = new Location(this.world, bossConfig.getValue().getLootChestX(),
                        bossConfig.getValue().getLootChestY(), bossConfig.getValue().getLootChestZ());

                BlockState state = lootChestLocation.getBlock().getState();
                if (state instanceof Chest) {
                    ((Chest) state).getInventory().clear();
                    spawnedBoss.setLootChestLocation(lootChestLocation);
                } else {
                    plugin.getLogger().warning("The given LootChest Location is not a Chest.");
                }

            }

            try {
                Boss<? extends Creature> boss = (Boss<? extends Creature>) type.get().getEntityClass().newInstance();

                boss.spawn(spawnLocation);
                spawnedBoss.setMonsterEntity(boss);

                this.bosses.put(boss.getEntity().getUniqueId(), spawnedBoss);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFightStart() {
        // this.bossBar = plugin.getServer().createBossBar("Title", BarColor.YELLOW,
        // BarStyle.SEGMENTED_12, arg3);
        // BarFlag.PLAY_BOSS_MUSIC
    }

    protected void updatePlayerHealthDisplay(Player player) {
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double health = player.getHealth();

        int healthPercent = (int) (100. * health / maxHealth);

        this.healthDisplayHead.getScore(player.getName()).setScore(healthPercent);

        int index = this.players.indexOf(player.getUniqueId()); // index mismatch with sidebar since removed player!!

        this.healthDisplaySideHandler.updateLine(index + 1,
                player.getName() + ": " + (int) health + "/" + (int) maxHealth);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            if (!player.getWorld().getName().equals(this.getWorld().getName()))
                return;

            // Update in next server tick so the health actually changed
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    updatePlayerHealthDisplay(player);
                }

            }, 1);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // Since the player may equipped a sword with +HP Attribute, his health will
        // change

        final Player player = (Player) event.getPlayer();

        if (!player.getWorld().getName().equals(this.getWorld().getName()))
            return;

        // Update in next server tick so the health actually changed
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                updatePlayerHealthDisplay(player);
            }

        }, 1);
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        // Update health if the player changed an armor slot
        if (event.isCancelled())
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;

        if (event.getAction() == InventoryAction.NOTHING)
            return;// Why does this get called if nothing happens??

        if (event.getSlotType() != SlotType.ARMOR && event.getSlotType() != SlotType.QUICKBAR
                && event.getSlotType() != SlotType.CONTAINER)
            return;

        if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;

        if (!event.getInventory().getType().equals(InventoryType.CRAFTING)
                && !event.getInventory().getType().equals(InventoryType.PLAYER))
            return;

        final Player player = (Player) event.getWhoClicked();

        if (!player.getWorld().getName().equals(this.getWorld().getName()))
            return;

        // Update in next server tick so the health actually changed
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                updatePlayerHealthDisplay(player);
            }

        }, 1);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            if (player.getGameMode() != GameMode.ADVENTURE)
                return;

            if (!player.getWorld().getName().equals(this.getWorld().getName()))
                return;

            if (event.getRegainReason() == RegainReason.SATIATED) {
                event.setCancelled(true);
                return;
            }

            // Update in next server tick so the health actually changed
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    updatePlayerHealthDisplay(player);
                }

            }, 1);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equals(this.getWorld().getName())) {
            removePlayer(event.getPlayer());
        } else if (event.getPlayer().getWorld().getName().equals(this.getWorld().getName())) {
            addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals(this.getWorld().getName())) {
            this.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getWorld().getName().equals(this.getWorld().getName())) {
            this.removePlayer(event.getPlayer());
        }
    }

    public void addPlayer(Player p) {
        if (this.players.contains(p.getUniqueId())) {
            return;
        }
        System.out.println("Add Player " + p.getName());

        this.players.add(p.getUniqueId());
        this.dungeonTeam.addEntry(p.getName());
        p.setScoreboard(this.scoreboard);
        updatePlayerHealthDisplay(p);
    }

    public void removePlayer(Player p) {
        if (!this.players.contains(p.getUniqueId())) {
            return;
        }
        System.out.println("remove Player: " + p.getName());

        int index = this.players.indexOf(p.getUniqueId());

        this.players.remove(p.getUniqueId());
        this.dungeonTeam.removeEntry(p.getName());
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

        // this.healthDisplaySideHandler.updateLine(index + 1, p.getName() + ": left");
        this.healthDisplaySideHandler.removeLine(index + 1);
    }

    public Optional<SpawnedMonster> findMonsterByID(UUID monsterID) {
        if (this.bosses.containsKey(monsterID))
            return Optional.of(this.bosses.get(monsterID));

        if (this.monsters.containsKey(monsterID))
            return Optional.of(this.monsters.get(monsterID));

        return Optional.empty();
    }

    /**
     * Removes the SpawnedMonster form the world and configuration
     * 
     * @param monsterID
     * @return
     */
    public boolean removeMonster(SpawnedMonster monster) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }
        // MonsterConfig mc = this.monstersConfiguration.get(sm.configID);
        monster.getMonsterEntity().getEntity().remove();
        this.monstersConfiguration.remove(monster.getConfigID());
        this.monsters.remove(monster.getMonsterEntity().getEntity().getUniqueId());

        configChanged = true;
        return true;
    }

    public boolean updateStrollingRadius(SpawnedMonster monster, float randomStrollRadius) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }

        // Since the monsters idleWalkSettings are references, the update inside the
        // config should also update the monsters ones.
        MonsterConfig monsterConfig = this.monstersConfiguration.get(monster.getConfigID());
        if (monsterConfig.getIdleWalkSettings() == null) {
            IdleWalkStrollSettings newSettings = new IdleWalkStrollSettings();
            newSettings.setMaxStrollRadius(randomStrollRadius);
            newSettings.setMinStrollDelay(20 * 3);
            newSettings.setMinStrollDelay(20 * 6);
            monsterConfig.setIdleWalkSettings(newSettings);

            IdleWalkStroll idleWalk = new IdleWalkStroll();
            idleWalk.setIdleWalkSettings(newSettings);
            idleWalk.setMonster(monster.getMonsterEntity());
            monster.getMonsterEntity().setIdleWalk(idleWalk);
        } else {
            if (!(monsterConfig.getIdleWalkSettings() instanceof IdleWalkStrollSettings)) {
                return false;
            }

            ((IdleWalkStrollSettings) monsterConfig.getIdleWalkSettings()).setMaxStrollRadius(randomStrollRadius);
        }
        configChanged = true;
        return true;
    }

    public boolean updateStrollingMinDelay(SpawnedMonster monster, int randomStrollMinDelay) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }

        // Since the monsters idleWalkSettings are references, the update inside the
        // config should also update the monsters ones.
        MonsterConfig monsterConfig = this.monstersConfiguration.get(monster.getConfigID());
        if (monsterConfig.getIdleWalkSettings() == null) {
            IdleWalkStrollSettings newSettings = new IdleWalkStrollSettings();
            newSettings.setMaxStrollRadius(5);
            newSettings.setMinStrollDelay(randomStrollMinDelay);
            newSettings.setMinStrollDelay(20 * 6);
            monsterConfig.setIdleWalkSettings(newSettings);

            IdleWalkStroll idleWalk = new IdleWalkStroll();
            idleWalk.setIdleWalkSettings(newSettings);
            idleWalk.setMonster(monster.getMonsterEntity());
            monster.getMonsterEntity().setIdleWalk(idleWalk);
        } else {
            if (!(monsterConfig.getIdleWalkSettings() instanceof IdleWalkStrollSettings)) {
                return false;
            }

            ((IdleWalkStrollSettings) monsterConfig.getIdleWalkSettings()).setMinStrollDelay(randomStrollMinDelay);
        }
        configChanged = true;
        return true;
    }

    public boolean updateStrollingMaxDelay(SpawnedMonster monster, int randomStrollMaxDelay) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }

        // Since the monsters idleWalkSettings are references, the update inside the
        // config should also update the monsters ones.
        MonsterConfig monsterConfig = this.monstersConfiguration.get(monster.getConfigID());
        if (monsterConfig.getIdleWalkSettings() == null) {
            IdleWalkStrollSettings newSettings = new IdleWalkStrollSettings();
            newSettings.setMaxStrollRadius(5);
            newSettings.setMinStrollDelay(20 * 3);
            newSettings.setMinStrollDelay(randomStrollMaxDelay);
            monsterConfig.setIdleWalkSettings(newSettings);

            IdleWalkStroll idleWalk = new IdleWalkStroll();
            idleWalk.setIdleWalkSettings(newSettings);
            idleWalk.setMonster(monster.getMonsterEntity());
            monster.getMonsterEntity().setIdleWalk(idleWalk);
        } else {
            if (!(monsterConfig.getIdleWalkSettings() instanceof IdleWalkStrollSettings)) {
                return false;
            }

            ((IdleWalkStrollSettings) monsterConfig.getIdleWalkSettings()).setMaxStrollDelay(randomStrollMaxDelay);
        }
        configChanged = true;
        return true;
    }

    public boolean addPatrolWaypoint(SpawnedMonster monster, Location loc) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }

        // Since the monsters idleWalkSettings are references, the update inside the
        // config should also update the monsters ones.
        MonsterConfig monsterConfig = this.monstersConfiguration.get(monster.getConfigID());
        if (monsterConfig.getIdleWalkSettings() == null) {
            IdleWalkPatrolSettings newSettings = new IdleWalkPatrolSettings();
            newSettings.getPatrolPoints().add(new PatrolWaypoint(loc.getX(), loc.getY(), loc.getZ(), 0));
            monsterConfig.setIdleWalkSettings(newSettings);

            IdleWalkPatrol idlePatrol = new IdleWalkPatrol();
            idlePatrol.setIdleWalkSettings(newSettings);
            idlePatrol.setMonster(monster.getMonsterEntity());
            monster.getMonsterEntity().setIdleWalk(idlePatrol);
        } else {
            if (!(monsterConfig.getIdleWalkSettings() instanceof IdleWalkPatrolSettings)) {
                return false;
            }

            ((IdleWalkPatrolSettings) monsterConfig.getIdleWalkSettings()).getPatrolPoints()
                    .add(new PatrolWaypoint(loc.getX(), loc.getY(), loc.getZ(), 0));
        }
        configChanged = true;
        return true;
    }

    public static void displayLine(World world, Vector loc1, Vector loc2) {
        double particleDist = 0.1;

        double amount = loc2.distance(loc1) / particleDist;

        Location currentPos = loc1.clone().toLocation(world);
        Vector dir = loc2.subtract(loc1);
        dir = dir.normalize().multiply(particleDist);

        for (int i = 0; i < amount; i++) {
            currentPos = currentPos.add(dir);
            world.spawnParticle(Particle.VILLAGER_HAPPY, currentPos, 1, 0, 0, 0);
        }
    }

    public static void displayFilledCircle(World world, Vector loc, float radius, float rasterX, float rasterZ) {
        double r2 = radius * radius;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        double r = 0;
        double g = 0.3;
        double b = 1;

        for (float dx = -radius; dx <= radius; dx += rasterX) {
            for (float dz = -radius; dz <= radius; dz += rasterZ) {
                if (dx * dx + dz * dz <= r2) {
                    world.spawnParticle(Particle.SPELL_INSTANT, x + dx, y, z + dz, 0, r, g, b);
                }
            }
        }
    }

    private static void hightLightWaypoints(World world, List<PatrolWaypoint> waypoints, boolean circle) {

        for (int i = 0; i < waypoints.size() - 1; i++) {
            Vector wp1 = waypoints.get(i).getVector();
            Vector wp2 = waypoints.get(i + 1).getVector();

            displayLine(world, wp1, wp2);
        }

        if (circle) {
            // also a line from last to first point
            Vector wp1 = waypoints.get(waypoints.size() - 1).getVector();
            Vector wp2 = waypoints.get(0).getVector();

            displayLine(world, wp1, wp2);
        }

    }

    public boolean highlightPatrolPath(SpawnedMonster monster) {
        if (!this.monsters.containsKey(monster.getMonsterEntity().getEntity().getUniqueId())) {
            return false;
        }

        if (!this.monstersConfiguration.containsKey(monster.getConfigID())) {
            return false;
        }

        MonsterConfig monsterConfig = this.monstersConfiguration.get(monster.getConfigID());
        if (monsterConfig.getIdleWalkSettings() == null
                || !(monsterConfig.getIdleWalkSettings() instanceof IdleWalkPatrolSettings)) {
            return false;
        }

        IdleWalkPatrolSettings patrolSettings = (IdleWalkPatrolSettings) monsterConfig.getIdleWalkSettings();

        hightLightWaypoints(monster.getMonsterEntity().getEntity().getWorld(), patrolSettings.getPatrolPoints(),
                patrolSettings.isPatrolCircle());
        return true;
    }

    public boolean setBossLootChest(SpawnedMonster monster, Block block) {
        if (!this.bosses.containsKey(monster.getMonsterEntity().getEntity().getUniqueId()))
            return false;

        if (!(block.getState() instanceof Chest))
            return false;

        SpawnedBoss boss = this.bosses.get(monster.getMonsterEntity().getEntity().getUniqueId());
        boss.setLootChestLocation(block.getLocation());
        MonsterConfig cfg = this.bossesConfiguration.get(boss.getConfigID());
        cfg.setLootChestX(block.getLocation().getBlockX());
        cfg.setLootChestY(block.getLocation().getBlockY());
        cfg.setLootChestZ(block.getLocation().getBlockZ());

        this.configChanged = true;

        return true;
    }

    @EventHandler
    public void onSpawnedMonsterDeath(MonsterDeathEvent event) {
        SpawnedMonster monster = null;
        if (this.monsters.containsKey(event.getMonster().getEntity().getUniqueId())) {
            monster = this.monsters.get(event.getMonster().getEntity().getUniqueId());
        } else if (this.bosses.containsKey(event.getMonster().getEntity().getUniqueId())) {
            monster = this.bosses.get(event.getMonster().getEntity().getUniqueId());
        }

        if (monster == null)
            return;

        monster.dropLoot();
    }

    @EventHandler
    public void onBossDeath(BossDeathEvent event) {
        // currently nothing to do here
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {

        if (!event.getBlock().getWorld().getName().equals(this.getWorld().getName())) {
            return;
        }

        if (event.getCause() == IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        if (!event.getBlock().getWorld().getName().equals(this.getWorld().getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!p.getGameMode().equals(GameMode.ADVENTURE))
            return;

        ItemStack itemInHand = event.getItem();
        if (plugin.getSkillFactory().isSkillItem(itemInHand)) {

            int currentSlot = p.getInventory().getHeldItemSlot();
            ItemStack i = p.getInventory().getItem(currentSlot);
            p.getWorld().dropItemNaturally(p.getLocation(), i);
            p.getInventory().setItem(currentSlot, null);

            p.getInventory().setHeldItemSlot(0);
            p.sendMessage("Your first item slot must not be a skill!");
            event.setCancelled(true);
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        // Check if the clicked block is a boss' loot chest
        Location blockLoc = event.getClickedBlock().getLocation();
        boolean isBossChest = false;
        for (SpawnedBoss entry : this.bosses.values()) {
            if (entry.getLootChestLocation() == null)
                continue;

            if (entry.getLootChestLocation().getBlockX() == blockLoc.getBlockX()
                    && entry.getLootChestLocation().getBlockY() == blockLoc.getBlockY()
                    && entry.getLootChestLocation().getBlockZ() == blockLoc.getBlockZ()) {
                isBossChest = true;
                // Do not open the chest if the boss is still alive
                if (!entry.getMonsterEntity().getEntity().isDead()) {
                    p.sendMessage(ChatColor.RED + "You have to kill the boss first!");
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // A player should not use (right click) any block (like chest, anvil...) in
        // adventure mode, if it is not a boss' loot chest
        if (!isBossChest) {
            Material mat = event.getClickedBlock().getType();
            boolean isClickedBlockInteractable = Utils.interactableBlocks.contains(mat);

            event.setCancelled(isClickedBlockInteractable);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;

        switch (event.getRightClicked().getType()) {
        case ARMOR_STAND:
            /* case BOAT: */
            /* case HORSE: */
        case ITEM_FRAME:
        case MINECART:
        case MINECART_CHEST:
        case MINECART_COMMAND:
        case MINECART_FURNACE:
        case MINECART_HOPPER:
        case MINECART_MOB_SPAWNER:
        case MINECART_TNT:
            /* case MULE: */
        case PAINTING:
            /* case ZOMBIE_HORSE: */
            /* case ZOMBIE_VILLAGER: */
            event.setCancelled(true);
        default:
            break;
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location deathLocation = event.getPlayer().getLocation();
        World deathWorld = deathLocation.getWorld();

        if (!deathWorld.getName().equals(this.world.getName())) {
            return;
        }

        event.setRespawnLocation(this.getPlayerSpawnLocation());

        // Update in next server tick so the health actually changed
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                updatePlayerHealthDisplay(event.getPlayer());
            }

        }, 1);
    }

}
