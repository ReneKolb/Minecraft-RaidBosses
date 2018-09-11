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
        fontWidth.put('�', 3);
        fontWidth.put('!', 1);
        fontWidth.put('"', 3);
        fontWidth.put('{', 3);
        fontWidth.put('}', 3);
        fontWidth.put('(', 3);
        fontWidth.put(')', 3);
        fontWidth.put('[', 3);
        fontWidth.put(']', 3);
        fontWidth.put('�', 2);
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
        // IMPROVE IDEA:
        // calibrate method: Send the client various chat messages: XXXXXXXX, each X is
        // clickable. The user has to click the X, where the linebreak in chat is. so
        // this marks the 219px.
        // Then calibrate 'space' width

        int pixelWidth = fullCharacterWidth * 6 - 1; // 5per Char + 1per inter-space
        int isWidth = getWidth(string);

        int mismatch = pixelWidth - isWidth;

        int addSpaces = Math.round((float) mismatch / (getPlainWidth(' ') + 1));
        return string + StringUtils.repeat(' ', addSpaces);
    }

}
