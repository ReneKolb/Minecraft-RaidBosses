package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
        return Arrays.asList(effect.getTootipText());
    }

}
