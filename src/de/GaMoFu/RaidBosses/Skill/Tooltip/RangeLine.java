package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class RangeLine implements ITooltipLine {

    private double range;

    private static final NumberFormat decimalFormat;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.0", otherSymbols);
    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.RANGE + "Range", SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = ChatColor.WHITE + decimalFormat.format(range);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
