package de.GaMoFu.RaidBosses.Items;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Attributes.Attributes.Attribute;
import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;

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

    public List<Attribute> getAttributes() {
        return Collections.emptyList();
    }

    // public void onDamageEntity(EntityDamageByEntityEvent event);

    public List<ItemEffect> getItemEffects() {
        return Collections.emptyList();
    }

    public short getDisplayDurability() {
        return 0;
    }

    public List<String> getLore() {
        return Collections.emptyList();
    }
}
