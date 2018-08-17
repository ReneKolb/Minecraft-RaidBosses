package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.md_5.bungee.api.ChatColor;

public class InstantHealLine implements ITooltipLine {

    private double healAmount;

    private static final NumberFormat decimalFormat;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.0", otherSymbols);
    }

    public InstantHealLine(double healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public List<String> formatLine() {
        String left = Colors.HEALTH + "Heal";
        String right = ChatColor.WHITE + decimalFormat.format(healAmount);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
