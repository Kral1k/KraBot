package com.kral1k.krabot.game.games.puzzle.editor;

import com.kral1k.krabot.game.games.puzzle.PuzzleContent;

import java.util.ArrayList;
import java.util.List;

public class PuzzlePackRiddleBuilder {
    private final String id;
    private String text;
    public final List<PuzzleContent.Answer> answerList = new ArrayList<>();

    public PuzzlePackRiddleBuilder(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public PuzzlePackRiddleBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public int answerSize() {
        return answerList.size();
    }

    public PuzzleContent.Answer getAnswer(int index) {
        return answerList.get(index);
    }

    public PuzzlePackRiddleBuilder setAnswer(int index, PuzzleContent.Answer answer) {
        answerList.set(index, answer);
        return this;
    }

    public PuzzlePackRiddleBuilder addAnswer(PuzzleContent.Answer answer) {
        answerList.add(answer);
        return this;
    }

    public PuzzleContent.Riddle build() {
        return new PuzzleContent.Riddle(text, answerList.toArray(PuzzleContent.Answer[]::new));
    }
}
