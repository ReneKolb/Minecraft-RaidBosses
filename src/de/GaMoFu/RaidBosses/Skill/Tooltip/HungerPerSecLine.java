package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

import de.GaMoFu.RaidBosses.Skill.SkillFactory;

public class HungerPerSecLine implements ITooltipLine {

    private int healAmount;

    private int tickDelay;

    private static final NumberFormat decimalFormatTime;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatTime = new DecimalFormat("0.00", otherSymbols);
    }

    public HungerPerSecLine(int healAmount, int tickDelay) {
        this.healAmount = healAmount;
        this.tickDelay = tickDelay;
    }

    @Override
    public List<String> formatLine() {
        String left = Colors.HUNGER + "Hunger";
        String right = ChatColor.GREEN + "+" + ChatColor.WHITE + healAmount + ChatColor.GOLD + " per " + ChatColor.WHITE
                + decimalFormatTime.format(tickDelay / 20.0) + ChatColor.GRAY + "sec";

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
