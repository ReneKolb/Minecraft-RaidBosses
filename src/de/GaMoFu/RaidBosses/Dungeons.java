package de.GaMoFu.RaidBosses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public class Dungeons {

    private enum EnumDungeons {
        //@formatter:off
		First(FirstInstance.class, FirstInstance.ALIAS),
		Intro(IntroDungeon.class, IntroDungeon.ALIAS);
		
		//@formatter:on

        private Class<? extends Dungeon> instanceClass;

        private String aliasName;

        private EnumDungeons(Class<? extends Dungeon> instanceClass, String aliasName) {
            this.instanceClass = instanceClass;
            this.aliasName = aliasName;
        }

        public Class<? extends Dungeon> getInstanceClass() {
            return this.instanceClass;
        }

        public static EnumDungeons fromAliasName(String aliasName) {
            for (EnumDungeons ei : EnumDungeons.values()) {
                if (ei.aliasName.equalsIgnoreCase(aliasName)) {
                    return ei;
                }
            }
            throw new RuntimeException("Cannot find InstanceClass from aliasName '" + aliasName + "'");
        }
    }

    private static final String CONFIG_DUNGEON_KEY = "instances";

    private List<DungeonConfig> instancesConfig;

    private Map<EnumDungeons, Dungeon> instances;

    private boolean configChanged;

    private RaidBosses plugin;

    public Dungeons(RaidBosses plugin) {
        this.configChanged = false;

        this.plugin = plugin;

        this.instances = new HashMap<>();

        loadInstances();
    }

    // Only for admins
    public void createNewInstance(World world, String instanceAlias) {
        EnumDungeons ei = EnumDungeons.fromAliasName(instanceAlias);

        if (this.instances.containsKey(ei)) {
            Dungeon i = this.instances.get(ei);
            plugin.getLogger()
                    .info("The world " + world.getName() + " is already assigned to instance " + i.getAlias());
            return;
        }

        plugin.getLogger().info("Creating new associated from '" + ChatColor.BLUE + world.getName() + ChatColor.RESET
                + "' to instance " + instanceAlias);

        try {
            Dungeon instance = ei.getInstanceClass().newInstance();
            instance.init(plugin, world);

            this.instances.put(ei, instance);

            DungeonConfig config = new DungeonConfig();
            config.setAliasName(instanceAlias);
            config.setWorldName(world.getName());
            this.instancesConfig.add(config);

            this.configChanged = true;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadInstances() {
        List<String> instancesConfig = plugin.getConfig().getStringList(CONFIG_DUNGEON_KEY);
        if (instancesConfig == null) {
            this.instancesConfig = new ArrayList<>();
            return;
        }

        this.instancesConfig = instancesConfig.stream().map(ic -> DungeonConfig.fromSaveString(ic))
                .collect(Collectors.toCollection(ArrayList::new));

        for (DungeonConfig ic : this.instancesConfig) {
            EnumDungeons ei = EnumDungeons.fromAliasName(ic.getAliasName());
            World world = plugin.getWorlds().getWorld(ic.getWorldName());

            try {
                // System.out.println("alias="+ic.getAliasName()+" EnumDungeons="+ei+"
                // class="+ei.getInstanceClass());
                Dungeon instance = ei.getInstanceClass().newInstance();
                instance.init(plugin, world);

                this.instances.put(ei, instance);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public Optional<Dungeon> findDungeonByMonster(UUID monsterID) {
        return this.instances.values().stream()
                .filter(dungeon -> dungeon.bosses.containsKey(monsterID) || dungeon.monsters.containsKey(monsterID))
                .findFirst();
    }

    public void saveInstancesList() {
        if (this.configChanged) {
            plugin.getLogger().info("Saving instances config");
            plugin.getConfig().set(CONFIG_DUNGEON_KEY, this.instancesConfig.stream().map(c -> c.toSaveString())
                    .collect(Collectors.toCollection(ArrayList::new)));
        } else {
            plugin.getLogger().info("Skip saving instances config. No changes.");
        }

        for (Dungeon instance : this.instances.values()) {
            instance.saveConfig();
        }
    }

    public Dungeon getFromAlias(String alias) {
        EnumDungeons inst = EnumDungeons.fromAliasName(alias);
        return this.instances.get(inst);
    }

    public Optional<Dungeon> getFromWorld(World world) {
        for (Dungeon inst : this.instances.values()) {
            if (world.getName().equals(inst.getWorld().getName())) {
                return Optional.of(inst);
            }
        }
        return Optional.empty();
    }

}
