package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class CooldownLine implements ITooltipLine {

    private int cooldownTicks;

    private static final NumberFormat decimalFormat;
    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("0.00", otherSymbols);
    }

    public CooldownLine(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;

    }

    @Override
    public List<String> formatLine() {
        String left = FontUtil.formatStringToWidth(Colors.COOLDOWN + "Cooldown",
                SkillTooltipBuilder.FIRST_COLUMN_WIDTH);
        String right = ChatColor.WHITE + decimalFormat.format(cooldownTicks / 20.0) + " " + ChatColor.GRAY + "sec ";

        return Arrays.asList(String.format(SkillTooltipBuilder.TABLE_FORMAT_PATTERN, left, right));
    }

}
