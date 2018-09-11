package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Attributes.Attributes;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Attributes.Attributes.AttributeType;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public abstract class Item {

    protected static final List<ChatColor> CHAT_COLORS = Arrays.asList(ChatColor.values()).stream()
            .filter(color -> color.isColor()).collect(Collectors.toList());

    protected static final List<ChatColor> RAINBOW_CHAT_COLORS = Collections
            .unmodifiableList(Arrays.asList(ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.AQUA,
                    ChatColor.DARK_RED, ChatColor.RED, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.DARK_GREEN,
                    ChatColor.GREEN, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE));

    protected ChatColor randomChatColor() {
        return CHAT_COLORS.get(RaidBosses.random.nextInt(CHAT_COLORS.size()));
    }

    private ChatColor getRainbowColorAt(int i) {
        i %= RAINBOW_CHAT_COLORS.size();
        return RAINBOW_CHAT_COLORS.get(i);
    }

    public abstract ItemTier getItemTier();

    protected abstract String getItemDisplayNameWithoutColor();

    public String getItemDisplayName() {
        if (getItemTier().getDisplayColor().equals(ChatColor.MAGIC)) {
            StringBuilder result = new StringBuilder();

            String name = getItemDisplayNameWithoutColor();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                result.append(getRainbowColorAt(i));
                result.append(c);
            }

            return result.toString();
        }
        return getItemTier().getDisplayColor() + getItemDisplayNameWithoutColor();
    }

    public abstract String getItemInternalName();

    public abstract Material getDisplayMaterial();

    public boolean isUnbreakable() {
        return true;
    }

    public List<Attribute> getAttributes() {
        return Collections.emptyList();
    }

    // public void onDamageEntity(EntityDamageByEntityEvent event);

    public List<ItemEffect> getItemEffects() {
        return Collections.emptyList();
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return new HashMap<>();
    }

    public void postProcessItemStack(ItemStack itemStack) {
        // A special Item may post process the created item Stack in order to
        // specializes the stack which makes no sense in a generic way, like applying
        // a pattern to a shield or staining leather armor. (this makes no sense for all
        // items and is shield / leatherArmor specific)
    }

    public short getDisplayDurability() {
        return 0;
    }

    public abstract SkillTooltipBuilder getTooltipBuilder();
    
    public String getNMSTagForTexturepackModel() {
        return null;
    }

    public static double getItemStackDamage(ItemStack stack) {
        double baseDamage = 1;
        
        switch (stack.getType()) {
        case WOODEN_SHOVEL:
            baseDamage = 1;
            break;
        case WOODEN_PICKAXE:
            baseDamage = 2;
            break;
        case WOODEN_AXE:
            baseDamage = 3;
            break;
        case WOODEN_SWORD:
            baseDamage = 4;
            break;

        case GOLDEN_SHOVEL:
            baseDamage = 1;
            break;
        case GOLDEN_PICKAXE:
            baseDamage = 2;
            break;
        case GOLDEN_AXE:
            baseDamage = 3;
            break;
        case GOLDEN_SWORD:
            baseDamage = 4;
            break;

        case STONE_SHOVEL:
            baseDamage = 2;
            break;
        case STONE_PICKAXE:
            baseDamage = 3;
            break;
        case STONE_AXE:
            baseDamage = 4;
            break;
        case STONE_SWORD:
            baseDamage = 5;
            break;

        case IRON_SHOVEL:
            baseDamage = 3;
            break;
        case IRON_PICKAXE:
            baseDamage = 4;
            break;
        case IRON_AXE:
            baseDamage = 5;
            break;
        case IRON_SWORD:
            baseDamage = 6;
            break;

        case DIAMOND_SHOVEL:
            baseDamage = 4;
            break;
        case DIAMOND_PICKAXE:
            baseDamage = 5;
            break;
        case DIAMOND_AXE:
            baseDamage = 6;
            break;
        case DIAMOND_SWORD:
            baseDamage = 7;
            break;
        }

        Attributes att = new Attributes(stack);
        List<Attribute> attributes = att.get(AttributeType.GENERIC_ATTACK_DAMAGE);

        for (Attribute attribute : attributes) {
            switch (attribute.getOperation()) {
            case ADD_NUMBER:
                baseDamage += attribute.getAmount();
                break;
            case ADD_PERCENTAGE:
                baseDamage *= (1 + attribute.getAmount());
                break;
            case MULTIPLY_PERCENTAGE:
                baseDamage *= attribute.getAmount();
                break;
            }
        }

        if (stack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
            // Add 1 extra dmg (one half heart) for the first level and 0.5 (a quarter
            // heart) for each additional level.
            int lvl = stack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            baseDamage += 1 + (lvl - 1) * 0.5;
        }

        return baseDamage;
    }
}
