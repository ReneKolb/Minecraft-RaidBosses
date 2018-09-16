package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;

import de.GaMoFu.RaidBosses.Items.Effects.ItemEffect;

public class ItemEffectLine implements ITooltipLine {

    private ItemEffect effect;

    public ItemEffectLine(ItemEffect effect) {
        this.effect = effect;
    }

    @Override
    public List<String> formatLine() {
        if (effect == null || StringUtils.isBlank(effect.getTootipText())) {
            return Collections.emptyList();
        }

        String[] lines = WordUtils
                .wrap(this.effect.getTootipText(), SkillTooltipBuilder.MAX_LINE_LENGTH - 2, "\n", true).split("\\n");

        List<String> result = new LinkedList<>();

        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                result.add(ChatColor.YELLOW + lines[i]);
            } else {
                result.add("  " + ChatColor.YELLOW + lines[i]);
            }
        }

        return result;
    }

}
