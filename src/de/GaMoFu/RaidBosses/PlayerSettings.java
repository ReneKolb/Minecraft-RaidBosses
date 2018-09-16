package de.GaMoFu.RaidBosses;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.Dungeons.Dungeon;
import de.GaMoFu.RaidBosses.Items.Item;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class PlayerSettings {

    public class MonsterSelection {

        private SpawnedMonster monster;

        private Dungeon dungeon;

        public MonsterSelection(SpawnedMonster monster, Dungeon dungeon) {
            this.monster = monster;
            this.dungeon = dungeon;
        }

        public SpawnedMonster getMonster() {
            return this.monster;
        }

        public Dungeon getDungeon() {
            return this.dungeon;
        }
    }

    public class BlockSelection {

        private Block block;

        private Dungeon dungeon;

        public BlockSelection(Block block, Dungeon dungeon) {
            this.block = block;
            this.dungeon = dungeon;
        }

        public Block getBlock() {
            return this.block;
        }

        public Dungeon getDungeon() {
            return this.dungeon;

        }
    }

    private class SkillCooldown {
        private ISkill skill;

        private int remainingCooldownSec;

        public SkillCooldown(ISkill skill) {
            this.skill = skill;
            this.remainingCooldownSec = Math.round(skill.getCooldownTicks() / 20f);
        }

        public ISkill getSkill() {
            return this.skill;
        }

        public int getRemainingCoolsownSec() {
            return this.remainingCooldownSec;
        }

        public void decRemainingCooldownSec() {
            if (this.remainingCooldownSec > 0)
                this.remainingCooldownSec -= 1;
        }
    }

    private enum ItemSlot {
        MAIN_HAND, OFF_HAND, HELMET, CHEST, LEGGINGS, BOOTS
    }

    private class ItemEffectTime {
        public int counter;

        public ItemEffectTime() {
            this.counter = 0;
        }

    }

    private MonsterSelection monsterSelection;
    private BlockSelection blockSelection;

    private Dungeon currentDungeon;

    private RaidBosses plugin;

    private int cooldownTimerID;

    private int loopTimerID;

    private Player player;

    // private Set<String> skillCooldowns;
    private Map<String, SkillCooldown> skillCooldowns;

    private Map<ItemSlot, ItemEffectTime> itemEffectTimes;

    public boolean selectLootchest;

    public PlayerSettings(RaidBosses plugin, Player player) {
        this.skillCooldowns = new HashMap<>();
        this.itemEffectTimes = new HashMap<>();

        this.player = player;
        this.plugin = plugin;
        this.selectLootchest = false;

        this.cooldownTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this.updateCooldowns, 1, 20);
        this.loopTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this.loopTimer, 1, 1);
    }

    public void onQuit() {
        Bukkit.getScheduler().cancelTask(this.cooldownTimerID);
        Bukkit.getScheduler().cancelTask(this.loopTimerID);
    }

    public void setMonsterSelection(SpawnedMonster monster, Dungeon dungeon) {
        this.monsterSelection = new MonsterSelection(monster, dungeon);
        this.blockSelection = null;
        this.selectLootchest = false;
    }

    public MonsterSelection getMonsterSelection() {
        return this.monsterSelection;
    }

    public BlockSelection getBlockSelection() {
        return blockSelection;
    }

    public void setBlockSelection(Block block, Dungeon dungeon) {
        this.blockSelection = new BlockSelection(block, dungeon);
        this.monsterSelection = null;
        this.selectLootchest = false;
    }

    public boolean isSkillReady(ISkill skill) {
        return !skillCooldowns.containsKey(skill.getSkillInternalName());
    }

    private HashSet<Integer> getSkillItemSlots(ISkill skill) {
        HashSet<Integer> result = new HashSet<Integer>();

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack is = player.getInventory().getItem(i);
            Optional<ISkill> oSkill = plugin.getSkillFactory().getSkillFromItemStack(is);
            if (!oSkill.isPresent())
                continue;

            if (oSkill.get().getSkillInternalName().equals(skill.getSkillInternalName())) {
                result.add(i);
            }
        }

        return result;
    }

    private void showCooldownEffect(ISkill skill, Collection<Integer> slots) {
        for (int i : slots) {
            ItemStack is = player.getInventory().getItem(i);
            is.setAmount(Math.round(skill.getCooldownTicks() / 20f));
            ItemMeta meta = is.getItemMeta();
            meta.removeEnchant(Enchantment.WATER_WORKER);
            is.setItemMeta(meta);
            player.getInventory().setItem(i, is);
        }
    }

    private void removeCooldownEffect(Collection<Integer> slots) {
        for (int i : slots) {
            ItemStack is = player.getInventory().getItem(i);
            is.setAmount(1); // nur zur sicherheit
            ItemMeta meta = is.getItemMeta();
            meta.addEnchant(Enchantment.WATER_WORKER, 1, true);
            is.setItemMeta(meta);
            player.getInventory().setItem(i, is);
        }
    }

    public void startSkillCooldown(ISkill skill) {
        if (skill.getCooldownTicks() > 0) {
            this.skillCooldowns.put(skill.getSkillInternalName(), new SkillCooldown(skill));

            final Collection<Integer> skillSlots = getSkillItemSlots(skill);

            showCooldownEffect(skill, skillSlots);
        }
    }

    private Runnable updateCooldowns = new Runnable() {

        @Override
        public void run() {
            List<ISkill> finishedCooldowns = new LinkedList<>();

            for (Map.Entry<String, SkillCooldown> entry : skillCooldowns.entrySet()) {
                SkillCooldown sd = entry.getValue();
                sd.decRemainingCooldownSec();

                if (sd.getRemainingCoolsownSec() > 0) {
                    Collection<Integer> slots = getSkillItemSlots(sd.getSkill());
                    for (int slot : slots) {
                        ItemStack itemStack = player.getInventory().getItem(slot);
                        itemStack.setAmount(sd.getRemainingCoolsownSec());
                        player.getInventory().setItem(slot, itemStack);
                    }
                } else {
                    finishedCooldowns.add(entry.getValue().getSkill());
                }

            }

            for (ISkill skill : finishedCooldowns) {
                removeCooldownEffect(getSkillItemSlots(skill));
                skillCooldowns.remove(skill.getSkillInternalName());
            }

        }

    };

    private Runnable loopTimer = new Runnable() {

        @Override
        public void run() {
            PlayerInventory inventory = player.getInventory();

            Optional<Item> helmet = plugin.getItemsFactory().getItemFromItemStack(inventory.getHelmet());
            Optional<Item> chest = plugin.getItemsFactory().getItemFromItemStack(inventory.getChestplate());
            Optional<Item> leggings = plugin.getItemsFactory().getItemFromItemStack(inventory.getLeggings());
            Optional<Item> boots = plugin.getItemsFactory().getItemFromItemStack(inventory.getBoots());
            Optional<Item> mainHand = plugin.getItemsFactory().getItemFromItemStack(inventory.getItemInMainHand());
            Optional<Item> offHand = plugin.getItemsFactory().getItemFromItemStack(inventory.getItemInOffHand());

            handlePassiveEffect(helmet, ItemSlot.HELMET);
            handlePassiveEffect(chest, ItemSlot.CHEST);
            handlePassiveEffect(leggings, ItemSlot.LEGGINGS);
            handlePassiveEffect(boots, ItemSlot.BOOTS);
            handlePassiveEffect(mainHand, ItemSlot.MAIN_HAND);
            handlePassiveEffect(offHand, ItemSlot.OFF_HAND);

        }

        private void handlePassiveEffect(Optional<Item> item, ItemSlot itemSlot) {
            if (!item.isPresent()) {
                return;
            }

            ItemEffectTime effTime;
            if (itemEffectTimes.containsKey(itemSlot)) {
                effTime = itemEffectTimes.get(itemSlot);
            } else {
                effTime = new ItemEffectTime();
                itemEffectTimes.put(itemSlot, effTime);
            }

            for (ItemEffect effect : item.get().getItemEffects()) {
                if (effect.getTickDelay() > 0) {
                    if (effTime.counter == 0) {
                        effect.onTick(player);
                        effTime.counter = effect.getTickDelay();
                    } else {
                        effTime.counter--;
                    }
                    // Only one passive effect per itemSlot (for now)
                    break;
                }
            }

        }

    };

    /**
     * @return the currentDungeon
     */
    public Dungeon getCurrentDungeon() {
        return currentDungeon;
    }

    /**
     * @param currentDungeon
     *            the currentDungeon to set
     */
    public void setCurrentDungeon(Dungeon currentDungeon) {
        this.currentDungeon = currentDungeon;
    }

    public void addSlowness(double amount, Operation op, int durationTicks) {
        // TODO Auto-generated method stub
        final AttributeModifier am = new AttributeModifier("Custom Slowness", amount, op);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(am);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(am);
            }

        }, durationTicks);

    }

    public void sendWelcomeMessage() {
        player.sendMessage("=========== " + ChatColor.BLUE + ChatColor.BOLD + "Welcome to Raid Bosses" + ChatColor.WHITE
                + ChatColor.RESET + " ============");
        player.sendMessage(
                "For the best possible experience use the ResourcePack Conquest and OptiFine with connected textures.");
        player.sendMessage("===============================================");
    }

}
