package de.GaMoFu.RaidBosses.EventListeners;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.Dungeon;
import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;

public class DungeonDesignListener implements Listener {

    private RaidBosses plugin;

    public DungeonDesignListener(RaidBosses plugin) {
        this.plugin = plugin;
    }

    public static final String MAGIC_WAND_ITEM_DISPLAY = ChatColor.AQUA + "Magic Wand";

    public static ItemStack buildMagicWand() {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();

        meta.setDisplayName(MAGIC_WAND_ITEM_DISPLAY);

        List<String> lore = Arrays.asList("Right click on monsters", "or blocks to select them.");
        meta.setLore(lore);

        wand.setItemMeta(meta);
        return wand;
    }

    public static boolean isMagicWand(ItemStack item) {
        return item != null && item.getType().equals(Material.STICK)
                && item.getItemMeta().getDisplayName().equals(MAGIC_WAND_ITEM_DISPLAY);

    }

    private void onSelectMonster(Player player, Creature creature) {
        Optional<Dungeon> dungeon = plugin.getInstances().findDungeonByMonster(creature.getUniqueId());
        if (!dungeon.isPresent())
            return;

        Optional<SpawnedMonster> monster = dungeon.get().findMonsterByID(creature.getUniqueId());
        if (!monster.isPresent())
            return;

        // not part of "select" but later
        // dungeon.get().modifyMonster();

        Creature c = monster.get().getMonsterEntity().getEntity();
        // monster.get().getMonsterEntity().getEntity().setVelocity(new Vector(0, 0.5,
        // 0));
        // monster.get().getMonsterEntity().getEntity().playEffect(EntityEffect.VILLAGER_HEART);
        c.getWorld().spawnParticle(Particle.HEART, c.getLocation().clone().add(0, 1, 0), 7, 0.5d, 1d, 0.5d);

        plugin.getPlayerSettings(player).setMonsterSelection(monster.get(), dungeon.get());

        player.sendMessage("selected Monster (configID=" + monster.get().getConfigID() + ") in dungeon: "
                + dungeon.get().getDisplayName());
    }

    private void onSelectBlock(Player player, Block block) {
        Optional<Dungeon> dungeon = plugin.getInstances().getFromWorld(block.getWorld());
        if (!dungeon.isPresent())
            return;

        Location loc = block.getLocation();
        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().clone().add(0.5, 1.2, 0.5), 7, 0.3d,
                0.15d, 0.3d);

        PlayerSettings ps = plugin.getPlayerSettings(player);
        if (ps.selectLootchest) {
            boolean success = dungeon.get().setBossLootChest(ps.getMonsterSelection().getMonster(), block);
            if (success) {
                player.sendMessage(ChatColor.GREEN + "Success");

            } else {
                player.sendMessage(ChatColor.RED + "Failure");
            }

            ps.selectLootchest = false;
        } else {
            ps.setBlockSelection(block, dungeon.get());

            player.sendMessage("selected Block (" + loc.getX() + "," + loc.getY() + "," + loc.getZ() + ") in dungeon: "
                    + dungeon.get().getDisplayName());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        Player p = event.getPlayer();
        if (!p.getGameMode().equals(GameMode.CREATIVE))
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        ItemStack item = p.getInventory().getItemInMainHand();
        if (isMagicWand(item)) {
            Entity clicked = event.getRightClicked();
            if (clicked instanceof Creature) {
                onSelectMonster(p, (Creature) clicked);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!p.getGameMode().equals(GameMode.CREATIVE))
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();
        if (isMagicWand(item)) {
            onSelectBlock(p, event.getClickedBlock());
        }
    }

}
