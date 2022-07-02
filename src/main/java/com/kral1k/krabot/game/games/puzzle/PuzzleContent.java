package com.kral1k.krabot.game.games.puzzle;

import java.util.List;
import java.util.StringJoiner;

public class PuzzleContent {
    private final List<Riddle> riddleList;

    public PuzzleContent(List<Riddle> riddleList) {
        this.riddleList = riddleList;
    }

    public List<Riddle> getRiddleList() {
        return riddleList;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Riddle riddle : riddleList) stringJoiner.add(riddle.toString());
        return String.format("{\"riddleList\":[%s]}", stringJoiner);
    }

    public static class Riddle {
        public final String text;
        public final Answer[] answers;

        public Riddle(String text, Answer... answers) {
            this.text = text;
            this.answers = answers;
        }

        @Override
        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (Answer answer : answers) stringJoiner.add(answer.toString());
            return String.format("{\"text\":\"%s\",\"answers\":[%s]}", text, stringJoiner);
        }
    }

    public static class Answer {
        public final String text;
        public final boolean correctly;

        public Answer(String text, boolean correctly) {
            this.text = text;
            this.correctly = correctly;
        }

        @Override
        public String toString() {
            return String.format("{\"text\":\"%s\",\"correctly\":%b}", text, correctly);
        }
    }
}
