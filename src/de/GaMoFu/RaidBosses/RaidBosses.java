package de.GaMoFu.RaidBosses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.GaMoFu.RaidBosses.Commands.CommandManager;
import de.GaMoFu.RaidBosses.Dungeons.Dungeons;
import de.GaMoFu.RaidBosses.Dungeons.PortalHandler;
import de.GaMoFu.RaidBosses.EventListeners.DungeonDesignListener;
import de.GaMoFu.RaidBosses.EventListeners.PlayerJoinListener;
import de.GaMoFu.RaidBosses.Items.ItemsFactory;
import de.GaMoFu.RaidBosses.Items.Effects.LightningEffect;
import de.GaMoFu.RaidBosses.Skill.SkillFactory;
import de.GaMoFu.RaidBosses.Trader.TraderFactory;
import de.GaMoFu.RaidBosses.Worlds.Worlds;

public final class RaidBosses extends JavaPlugin {

    // TODO: Mob Spawnen / add via click
    // TODO: select spawned mob via click
    // TODO: Modify selected mob, change Type, delete, move, rotate

    private CommandManager commandManager;

    private static RaidBosses pluginInstance;

    private Worlds worlds;

    private Dungeons instances;

    private CraftingHandler craftingHandler;

    private SkillFactory skillFactory;

    private ItemsFactory itemsFactory;

    private TraderFactory traderFactory;

    private HologramHandler hologramHandler;

    private FallingSwordHandler fallingSwordHandler;

    private ProjectileManager projectileManager;

    private AreaCloudHandler areaCloudHandler;

    private CustomDamageHandler customDamageHandler;

    private PortalHandler portalHandler;

    private Map<UUID, PlayerSettings> playerSettings;

    public static final Random random = new Random();

    public static DecimalFormat df;

    private void printAsciiArt() {

        this.getLogger().info("");

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/ascii/all.cfg")));
        List<String> enabledFiles = bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));

        if (enabledFiles == null || enabledFiles.isEmpty()) {
            this.getLogger().info("#####################################################");
            this.getLogger().info("#################### RAID BOSSES ####################");
            this.getLogger().info("#####################################################");
            return;
        }

        // System.out.println("\033[31;1mHello\033[0m, \033[32;1;2mworld!\033[0m");
        // System.out.println("\033[31mRed\033[32m, Green\033[33m, Yellow\033[34m,
        // Blue\033[0m");

        String useName = enabledFiles.get(new Random().nextInt(enabledFiles.size()));

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/ascii/" + useName)));
            String line;
            while ((line = br.readLine()) != null) {
                this.getLogger().info(line);
            }
            this.getLogger().info("");
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.worlds.saveWorldsList();
        this.instances.saveInstancesList();
        this.saveConfig();
        this.getLogger().info("RaidBosses has been disabled");
    }

    @Override
    public void onEnable() {
        this.printAsciiArt();

        this.hologramHandler = new HologramHandler(this);

        df = new DecimalFormat("#.0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));

        pluginInstance = this;

        this.craftingHandler = new CraftingHandler(this);

        // must be initialized before instances
        this.worlds = new Worlds(this);

        this.instances = new Dungeons(this);

        this.commandManager = new CommandManager(this);

        this.playerSettings = new HashMap<>();

        for (Player p : this.getServer().getOnlinePlayers()) {
            this.playerSettings.put(p.getUniqueId(), new PlayerSettings(this, p));
        }

        this.skillFactory = new SkillFactory(this);

        this.itemsFactory = new ItemsFactory(this);

        this.traderFactory = new TraderFactory(this);

        this.fallingSwordHandler = new FallingSwordHandler(this);

        this.projectileManager = new ProjectileManager(this);

        this.areaCloudHandler = new AreaCloudHandler(this);

        // this.customDamageHandler = new CustomDamageHandler(this);

        this.portalHandler = new PortalHandler(this);

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getServer().getPluginManager().registerEvents(new DungeonDesignListener(this), this);

        this.getServer().getPluginManager().registerEvents(LightningEffect.INSTANCE, this);

        this.getLogger().info("RaidBosses has benn enabled");

        // player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
        // TextComponent.fromLegacyText("Test"));
    }

    public static RaidBosses getPluginInstance() {
        return pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.getLogger().info(
                sender.getName() + " issued command: " + command.getName() + " with args: " + Arrays.toString(args));
        return this.commandManager.onCommand(this, sender, command, label, args);
    }

    public Worlds getWorlds() {
        return this.worlds;
    }

    public Dungeons getInstances() {
        return this.instances;
    }

    public SkillFactory getSkillFactory() {
        return this.skillFactory;
    }

    public ItemsFactory getItemsFactory() {
        return this.itemsFactory;
    }

    public TraderFactory getTraderFactory() {
        return this.traderFactory;
    }

    public HologramHandler getHologramHandler() {
        return this.hologramHandler;
    }

    public FallingSwordHandler getFallingSwordHandler() {
        return this.fallingSwordHandler;
    }

    public ProjectileManager getProjectileManager() {
        return this.projectileManager;
    }

    public AreaCloudHandler getAreaCloudHandler() {
        return this.areaCloudHandler;
    }

    public CustomDamageHandler getCustomDamageHandler() {
        return this.customDamageHandler;
    }

    public PortalHandler getPortalHandler() {
        return this.portalHandler;
    }

    public void addNewPlayerSettings(Player player) {
        PlayerSettings playerSettings = new PlayerSettings(this, player);
        this.playerSettings.put(player.getUniqueId(), playerSettings);
        playerSettings.sendWelcomeMessage();
    }

    public PlayerSettings getPlayerSettings(Player player) {
        return this.playerSettings.get(player.getUniqueId());
    }

    public void removePlayerSettings(Player player) {
        PlayerSettings ps = this.playerSettings.get(player.getUniqueId());
        ps.onQuit();
        this.playerSettings.remove(player.getUniqueId());
    }

}
