package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class SkillTooltipBuilder {

    public static final int MAX_LINE_LENGTH = 30;

    public static final int FIRST_COLUMN_WIDTH = 11;

    public static final String TABLE_FORMAT_PATTERN = "%s" + ChatColor.GRAY + ": %s";

    private static final NumberFormat decimalFormatTimeSec;
    private static final NumberFormat decimalFormatHealth;
    private static final NumberFormat decimalFormatDamage;
    private static final NumberFormat decimalFormatDistance;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatTimeSec = new DecimalFormat(ChatColor.WHITE + "0.##" + ChatColor.GRAY + "s", otherSymbols);
        decimalFormatHealth = new DecimalFormat(ChatColor.WHITE + "0.0" + ChatColor.RED + "♥", otherSymbols);
        decimalFormatDamage = new DecimalFormat(ChatColor.WHITE + "0.#" + ChatColor.GRAY + "♥", otherSymbols);
        decimalFormatDistance = new DecimalFormat(ChatColor.WHITE + "0.0" + ChatColor.GRAY + "m", otherSymbols);
    }

    public static String formatTimeSec(int ticks) {
        return decimalFormatTimeSec.format(ticks / 20.0);
    }

    public static String formatHealth(double halfHearts) {
        return decimalFormatHealth.format(halfHearts / 2.0);
    }

    public static String formatDamage(double halfHearts) {
        return decimalFormatDamage.format(halfHearts / 2.0);
    }

    public static String formatDistance(double meters) {
        return decimalFormatDistance.format(meters);
    }

    private List<ITooltipLine> lines;

    public SkillTooltipBuilder() {
        this.lines = new LinkedList<>();
    }

    public SkillTooltipBuilder add(ITooltipLine line) {
        lines.add(line);
        return this;
    }

    public List<String> build() {
        List<String> result = new LinkedList<>();
        for (ITooltipLine line : lines) {
            result.addAll(line.formatLine());
        }
        return result;
    }

    public boolean isEmpty() {
        return this.lines.isEmpty();
    }

    public ITooltipLine getLastLine() {
        if (lines.size() == 0)
            return null;

        return lines.get(lines.size() - 1);
    }

}
