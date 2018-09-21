package de.GaMoFu.RaidBosses.Skill;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Items.Item;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class SkillFactory implements Listener {

    private RaidBosses plugin;

    /** For looking up a skill by its internal name */
    private Map<String, ISkill> skillNameLookup; // SkillName -> Skill

    private List<String> internalSkillNames;

    /** For looking up a skill be an held item's display name */
    private Map<String, ISkill> skillDisplayNameLookup; // DisplayString -> Skill

    public SkillFactory(RaidBosses plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.skillNameLookup = new HashMap<>();
        this.skillDisplayNameLookup = new HashMap<>();

        init();
    }

    public List<String> getSkillInternalNames() {
        if (internalSkillNames == null) {
            internalSkillNames = new LinkedList<>(skillNameLookup.keySet());
        }
        return internalSkillNames;
    }

    public ISkill getSkillFromInternalName(String skillName) {
        return skillNameLookup.get(skillName);
    }

    private void init() {
        this.plugin.getLogger().info("Initializing skills");

        for (SkillsEnum s : SkillsEnum.values()) {
            Class<? extends ISkill> skillClass = s.getSkillClass();
            ISkill skill;
            try {
                skill = skillClass.newInstance();

                this.skillNameLookup.put(skill.getSkillInternalName(), skill);
                this.skillDisplayNameLookup.put(skill.getSkillDisplayName(), skill);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isSkillItem(ItemStack item) {
        if (item == null)
            return false;

        ItemMeta meta = item.getItemMeta();
        String displayName = meta.getDisplayName();
        return skillDisplayNameLookup.containsKey(displayName);
    }

    public Optional<ISkill> getSkillFromItemStack(ItemStack item) {
        if (item == null)
            return Optional.empty();

        ItemMeta meta = item.getItemMeta();
        String displayName = meta.getDisplayName();
        if (skillDisplayNameLookup.containsKey(displayName)) {
            return Optional.of(skillDisplayNameLookup.get(displayName));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ItemStack> buildSkillItem(String skillName) {

        ISkill skill = skillNameLookup.get(skillName);
        if (skill == null) {
            return Optional.empty();
        }

        ItemStack result = new ItemStack(skill.getDisplayMaterial(), 1, skill.getDisplayDurability());

        ItemMeta meta = result.getItemMeta();

        meta.setDisplayName(skill.getSkillDisplayName());

        SkillTooltipBuilder tooltipBuilder = skill.getTooltipBuilder();
        if (tooltipBuilder != null) {
            meta.setLore(tooltipBuilder.build());
        }

        meta.addEnchant(Enchantment.WATER_WORKER, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        result.setItemMeta(meta);
        return Optional.of(result);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        int newSlot = event.getNewSlot();
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }

        ItemStack newItem = player.getInventory().getItem(newSlot);
        Optional<ISkill> oSkill = getSkillFromItemStack(newItem);

        if (!oSkill.isPresent())
            return;

        event.setCancelled(true);

        ISkill skill = oSkill.get();

        PlayerSettings ps = plugin.getPlayerSettings(player);

        ps.handleUseSkill(skill);
    }

    @EventHandler
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

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Optional<Item> item = plugin.getItemsFactory().getItemFromItemStack(itemInHand);
            if (item.isPresent()) {
                for (ItemEffect effect : item.get().getItemEffects()) {
                    effect.onInteract(p);
                }

                event.setCancelled(true);
            }
        }
    }

}
