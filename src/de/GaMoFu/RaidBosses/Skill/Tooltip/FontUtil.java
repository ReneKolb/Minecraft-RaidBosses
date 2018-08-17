package de.GaMoFu.RaidBosses.Skill.Tooltip;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class FontUtil {

    private static final Map<Character, Integer> fontWidth = new HashMap<>();
    static {
        fontWidth.put('I', 3);

        fontWidth.put('i', 1);
        fontWidth.put('l', 2);
        fontWidth.put('t', 3);

        fontWidth.put(',', 1);
        fontWidth.put(';', 1);
        fontWidth.put('.', 1);
        fontWidth.put(':', 1);
        fontWidth.put('<', 4);
        fontWidth.put('>', 4);
        fontWidth.put(' ', 3);
        fontWidth.put('|', 1);
        fontWidth.put('\'', 1);
        fontWidth.put('*', 3);
        fontWidth.put('°', 3);
        fontWidth.put('!', 1);
        fontWidth.put('"', 3);
        fontWidth.put('{', 3);
        fontWidth.put('}', 3);
        fontWidth.put('(', 3);
        fontWidth.put(')', 3);
        fontWidth.put('[', 3);
        fontWidth.put(']', 3);
        fontWidth.put('´', 2);
        fontWidth.put('`', 2);

    }

    public static int getPlainWidth(char character) {
        Integer width = fontWidth.get(character);

        if (width == null)
            return 5;

        return width;
    }

    public static int getWidth(String string) {
        int width = 0;
        for (int i = 0; i < string.length(); i++) {
            width += getPlainWidth(string.charAt(i)) + 1;
        }

        if (width > 0)
            width--; // remove last inter-space

        return width;
    }

    public static String formatStringToWidth(String string, int fullCharacterWidth) {
        // pixelWidth=65

        int pixelWidth = fullCharacterWidth * 6 - 1; // 5per Char + 1per inter-space
        int isWidth = getWidth(string);

        System.out.println("string=" + string + " wid=" + isWidth + " target= " + pixelWidth);

        int mismatch = pixelWidth - isWidth;
        System.out.println("mismatch=" + mismatch);

        int addSpaces = Math.round((float) mismatch / (getPlainWidth(' ') + 1));
        System.out.println("spaces= " + addSpaces + " wid= " + getWidth(StringUtils.repeat(' ', addSpaces)));
        return string + StringUtils.repeat(' ', addSpaces);
    }

}
