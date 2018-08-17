package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class HealPerSecLine implements ITooltipLine {

    private double healAmount;

    private int tickDelay;

    private static final NumberFormat decimalFormatTime;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatTime = new DecimalFormat("0.00", otherSymbols);
    }

    private static final NumberFormat decimalFormatHealth;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatHealth = new DecimalFormat("0.0", otherSymbols);
    }

    public HealPerSecLine(double healAmount, int tickDelay) {
        this.healAmount = healAmount;
        this.tickDelay = tickDelay;
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.HEALTH + "Heal", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = ChatColor.WHITE + decimalFormatHealth.format(healAmount) + Colors.PER_SECOND + " per "
                + ChatColor.WHITE + decimalFormatTime.format(tickDelay / 20.0) + ChatColor.GRAY + "sec";

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
