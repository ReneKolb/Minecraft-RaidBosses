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
        return Arrays.asList(ChatColor.LIGHT_PURPLE + "  Heal" + ChatColor.GRAY + ": " + ChatColor.WHITE
                + decimalFormatHealth.format(healAmount) + ChatColor.GOLD + " per " + ChatColor.WHITE
                + decimalFormatTime.format(tickDelay / 20.0) + ChatColor.GRAY + "sec");
    }

}
