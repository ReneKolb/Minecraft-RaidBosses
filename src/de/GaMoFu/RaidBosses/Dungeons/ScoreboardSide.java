package de.GaMoFu.RaidBosses.Dungeons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import de.GaMoFu.RaidBosses.RaidBosses;

public class ScoreboardSide {

    private class UniqueString {
        public static final int LINE_LENGTH = 25;

        // private String originalString;

        private String string;

        private Random rnd;

        private ChatColor randomChatColor() {
            return ChatColor.values()[rnd.nextInt(ChatColor.values().length)];
        }

        private String makeUniqueString(String input) {
            String result = input;
            while (result.length() < LINE_LENGTH) {
                result += randomChatColor();
            }
            return result;
        }

        public UniqueString(String s) {
            this.rnd = new Random();
            // this.originalString = s;
            this.string = makeUniqueString(s);
        }

        // public String getOriginalString() {
        // return this.originalString;
        // }

        public String getString() {
            return this.string;
        }

        @Override
        public String toString() {
            return this.getString();
        }
    }

    private int linesAmount;

    private List<UniqueString> lines;

    // private RaidBosses plugin;

    private Scoreboard scoreboard;
    private Objective sideBarObjective;

    public ScoreboardSide(RaidBosses plugin, Scoreboard scorebaord, Objective objective, int linesAmount) {
        // this.plugin = plugin;

        this.linesAmount = linesAmount;

        this.lines = new ArrayList<>();
        for (int i = 0; i < linesAmount; i++) {
            this.lines.add(new UniqueString(""));
        }

        this.scoreboard = scorebaord;
        this.sideBarObjective = objective;

        for (int i = linesAmount; i > 0; i--) {
            // The setScore is for the ordering
            this.sideBarObjective.getScore(this.lines.get(linesAmount - i).getString()).setScore(i);
        }

    }

    public void updateLine(int line, String newString) {
        String oldString = this.lines.get(line - 1).getString();

        this.scoreboard.resetScores(oldString);

        UniqueString newLine = new UniqueString(newString);
        this.lines.set(line - 1, newLine);
        this.sideBarObjective.getScore(newLine.getString()).setScore(linesAmount - line + 1);
    }

    public void removeLine(int line) {
        // No need to reset and re-add scores, only change the score points and the
        // sorting is well done.
        for (int i = line + 1; i <= this.linesAmount; i++) {
            UniqueString shiftLine = this.lines.get(i - 1);
            this.sideBarObjective.getScore(shiftLine.getString()).setScore(linesAmount - i + 1 + 1);
        }

        UniqueString oldLine = this.lines.get(line - 1);
        this.scoreboard.resetScores(oldLine.getString());
        this.lines.remove(line - 1);

        UniqueString appendLine = new UniqueString("");
        this.lines.add(appendLine);
        this.sideBarObjective.getScore(appendLine.getString()).setScore(1);
    }

}
