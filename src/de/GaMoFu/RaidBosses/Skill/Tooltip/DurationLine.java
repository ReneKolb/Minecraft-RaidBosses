package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class DurationLine implements ITooltipLine {

    private int durationTicks;

    private static final NumberFormat decimalFormat;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.00", otherSymbols);
    }

    public DurationLine(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    @Override
    public List<String> formatLine() {
        String left = Colors.DURATION + "Duration";
        String right = ChatColor.WHITE + decimalFormat.format(durationTicks / 20.0) + ChatColor.GRAY + "sec";

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
