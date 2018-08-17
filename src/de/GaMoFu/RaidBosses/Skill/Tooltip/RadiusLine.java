package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class RadiusLine implements ITooltipLine {

    private double radius;

    private static final NumberFormat decimalFormat;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.0", otherSymbols);
    }

    public RadiusLine(double radius) {
        this.radius = radius;
    }

    @Override
    public List<String> formatLine() {
        String left = Colors.RADIUS + "Radius";
        String right = ChatColor.WHITE + decimalFormat.format(radius);

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
